package org.dg.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.dg.dto.FormItem;
import org.dg.dto.FormItemId;
import org.dg.dto.Formulaire;
import org.dg.dto.TypeFormulaire;
import org.dg.dto.items.FImage;
import org.dg.dto.items.FList;
import org.dg.dto.items.FListElement;
import org.dg.dto.items.FProduct;
import org.dg.dto.items.FQuestion;
import org.dg.dto.items.FText;
import org.dg.errors.ShopsErrors;
import org.dg.errors.ShopsException;
import org.dg.repositories.FormulaireRepository;
import org.dg.repositories.TypeFormulaireRepository;
import org.dg.utils.FormItemUtils;

import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.api.services.forms.v1.model.BatchUpdateFormRequest;
import com.google.api.services.forms.v1.model.ChoiceQuestion;
import com.google.api.services.forms.v1.model.CreateItemRequest;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.forms.v1.model.FormSettings;
import com.google.api.services.forms.v1.model.Image;
import com.google.api.services.forms.v1.model.ImageItem;
import com.google.api.services.forms.v1.model.Info;
import com.google.api.services.forms.v1.model.Item;
import com.google.api.services.forms.v1.model.ListFormResponsesResponse;
import com.google.api.services.forms.v1.model.Location;
import com.google.api.services.forms.v1.model.Option;
import com.google.api.services.forms.v1.model.Question;
import com.google.api.services.forms.v1.model.QuestionItem;
import com.google.api.services.forms.v1.model.QuizSettings;
import com.google.api.services.forms.v1.model.Request;
import com.google.api.services.forms.v1.model.TextItem;
import com.google.api.services.forms.v1.model.TextQuestion;
import com.google.api.services.forms.v1.model.UpdateFormInfoRequest;
import com.google.api.services.forms.v1.model.UpdateSettingsRequest;

import io.quarkus.logging.Log;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FormulairesService {

    private static final String READER_ROLE = "reader";
    private final CredentialService credentialService;
    private final FormulaireRepository formulaireRepository;
    private final TypeFormulaireRepository typeFormulaireRepository;
    private final DriveService driveService;

    @Inject
    public FormulairesService(CredentialService credentialService, FormulaireRepository formulaireRepository,
            DriveService driveService, TypeFormulaireRepository typeFormulaireRepository) {
        this.credentialService = credentialService;
        this.formulaireRepository = formulaireRepository;
        this.driveService = driveService;
        this.typeFormulaireRepository = typeFormulaireRepository;
    }

    private void updateFormInfo(Formulaire formulaire) {
        BatchUpdateFormRequest batchRequest = new BatchUpdateFormRequest();
        UpdateFormInfoRequest updateFormInfoRequest = new UpdateFormInfoRequest();
        Request request = new Request();
        Info info = new Info();
        Optional<FormItem> formItem = formulaire.getTypeFormulaire().getFormItem(FormItemId.ENTETE);
        if (formItem.isPresent()) {
            FText text = (FText) formItem.get();
            info.setTitle(text.getTitre());
            info.setDocumentTitle(text.getTitre());
            info.setDescription(text.getTexte());
        } else {
            throw new ShopsException(ShopsErrors.GENERATION_ERROR);
        }
        updateFormInfoRequest.setInfo(info);
        updateFormInfoRequest.setUpdateMask("*");
        request.setUpdateFormInfo(updateFormInfoRequest);

        batchRequest.setRequests(Collections.singletonList(request));

        try {
            credentialService.getFormsService().forms().batchUpdate(formulaire.getFormId(), batchRequest).execute();
        } catch (IOException e) {
            throw new ShopsException(ShopsErrors.GENERATION_ERROR, "Erreur lors de updateFormInfo", e);
        }
    }

    private Request buildRequest(Item item, Integer index) {
        Request request = new Request();
        request.setCreateItem(new CreateItemRequest());
        request.getCreateItem().setItem(item);
        request.getCreateItem().setLocation(new Location());
        request.getCreateItem().getLocation().setIndex(index);
        return request;
    }

    private Request buildImage(FImage fImage, Integer index) {
        Image image = new Image();
        image.setSourceUri(fImage.getImageUri());

        Item item = new Item();
        item.setTitle(fImage.getTitre());
        item.setItemId(fImage.getItemId());
        item.setImageItem(new ImageItem());
        item.getImageItem().setImage(image);

        return buildRequest(item, index);
    }

    private Request buildText(FText fText, Integer index) {
        Item item = new Item();
        item.setItemId(fText.getItemId());
        item.setTitle(fText.getTitre());
        item.setDescription(fText.getTexte());
        item.setTextItem(new TextItem());

        return buildRequest(item, index);
    }

    private Request buildQuestion(FQuestion fQuestion, Integer index) {
        Question question = new Question();
        question.setRequired(true);
        question.setTextQuestion(new TextQuestion());
        question.getTextQuestion().setParagraph(false);

        Item item = new Item();
        item.setTitle(fQuestion.getTitre());
        item.setItemId(fQuestion.getItemId());
        item.setQuestionItem(new QuestionItem());
        item.getQuestionItem().setQuestion(question);

        return buildRequest(item, index);
    }

    private Question buildQuestionWithChoice(boolean required, String type, List<Option> options) {
        Question question = new Question()
                .setRequired(required)
                .setChoiceQuestion(new ChoiceQuestion());
        question.getChoiceQuestion().setType(type);
        question.getChoiceQuestion().setOptions(options);
        return question;
    }

    private Request buildProduct(String itemId, FProduct fProduct, Integer index) {
        Item item = new Item();
        item.setTitle(fProduct.getTitre());

        QuestionItem questionItem = new QuestionItem();
        Image image = new Image();
        image.setSourceUri(fProduct.getImageUri());
        questionItem.setImage(image);
        item.setQuestionItem(questionItem);

        List<Option> options = new ArrayList<>();
        for (String element : fProduct.getQuantites()) {
            Option option = new Option();
            option.setValue(element);
            options.add(option);
        }

        item.getQuestionItem().setQuestion(buildQuestionWithChoice(false, fProduct.getTypeSelection(), options));

        return buildRequest(item, index);
    }

    /**
     * Contruit un item liste avec des éléments seletionnables
     *
     * @param fList buffer de FList
     * @param index Indice donnant la position dans l'affichage
     * @return La requete pour le batchUpdate
     */
    private Request buildList(FList fList, Integer index) {
        Item item = new Item();
        item.setTitle(fList.getTitre());
        item.setItemId(fList.getItemId());

        item.setQuestionItem(new QuestionItem());

        List<Option> options = new ArrayList<>();
        for (FListElement element : fList.getListeChoix()) {
            Option option = new Option();
            option.setValue(element.getLibelleChoix());

            if (!StringUtil.isNullOrEmpty(element.getImageUri())) {
                Image image = new Image();
                image.setSourceUri(element.getImageUri());
                option.setImage(image);
            }

            options.add(option);
        }
        item.getQuestionItem().setQuestion(buildQuestionWithChoice(true, fList.getType(), options));

        return buildRequest(item, index);
    }

    private void addItemsToForm(Formulaire formulaire) {

        BatchUpdateFormRequest batchRequest = new BatchUpdateFormRequest();
        List<Request> requests = new ArrayList<>();

        int cpt = 0;
        for (FormItem formItem : formulaire.getTypeFormulaire().getFormItems()) {
            if (formItem.isChecked()) {
                switch (formItem.getName()) {
                    case "ENTETE":
                        break;
                    case "IMAGE_BANDEAU", "LOCALISATION":
                        FImage fImage = (FImage) formItem;
                        Log.debug("fImage = " + fImage);
                        requests.add(buildImage(fImage, cpt));
                        cpt++;
                        break;
                    case "E_MAIL", "NOM_PRENOM":
                        FQuestion fQuestion = (FQuestion) formItem;
                        Log.debug("fQuestion = " + fQuestion);
                        requests.add(buildQuestion(fQuestion, cpt));
                        cpt++;
                        break;
                    case "PANIER", "CAGETTE", "FORMULE":
                        FList fList = (FList) formItem;
                        Log.debug("fList = " + fList);
                        requests.add(buildList(fList, cpt));
                        cpt++;
                        break;
                    case "COMPOSITION":
                        FText fText = (FText) formItem;
                        Log.debug("fText = " + fText);
                        requests.add(buildText(fText, cpt));
                        cpt++;
                        break;
                    case "FProduct":
                        // FProduct fProduct = (FProduct) formItem;
                        // requests.add(buildProduct(formItem.getId(), fProduct, cpt));
                        // cpt++;
                        break;
                    default:
                        Log.error("Item non trouvé = " + formItem.getName());
                        break;
                }
            }
        }

        batchRequest.setRequests(requests);
        try {
            credentialService.getFormsService().forms().batchUpdate(formulaire.getFormId(), batchRequest).execute();
        } catch (IOException e) {
            throw new ShopsException(ShopsErrors.GENERATION_ERROR, "Erreur lors de addItemsToForm", e);
        }
    }

    private void transformInQuiz(String formId) {
        BatchUpdateFormRequest batchRequest = new BatchUpdateFormRequest();
        Request request = new Request();

        request.setUpdateSettings(new UpdateSettingsRequest());
        request.getUpdateSettings().setSettings(new FormSettings());
        request.getUpdateSettings().getSettings().setQuizSettings(new QuizSettings());
        request.getUpdateSettings().getSettings().getQuizSettings().setIsQuiz(true);
        request.getUpdateSettings().setUpdateMask("quizSettings.isQuiz");
        batchRequest.setRequests(Collections.singletonList(request));

        Log.debug("batchRequest = " + batchRequest.toString());

        try {
            credentialService.getFormsService().forms().batchUpdate(formId, batchRequest).execute();
        } catch (IOException e) {
            throw new ShopsException(ShopsErrors.GENERATION_ERROR, "Erreur lors de transformInQuiz", e);
        }
    }

    public Formulaire createNewForm(Formulaire formulaire) {
        long startTime = System.currentTimeMillis();

        // formulaire.addFormulaireItems(new FProduct(PRODUIT_TITRE, PRODUIT_IMAGE_URI,
        // PRODUITS_QUANTITES,
        // PRODUIT_TYPE_SELECTION));

        Form form = new Form();
        Info info = new Info();
        Optional<FormItem> formItem = formulaire.getTypeFormulaire().getFormItem(FormItemId.ENTETE);
        if (formItem.isPresent()) {
            FText text = (FText) formItem.get();
            info.setTitle(text.getTitre());
            info.setDocumentTitle(text.getTitre());
        } else {
            throw new ShopsException(ShopsErrors.GENERATION_ERROR);
        }

        form.setInfo(info);

        try {
            form = credentialService.getFormsService().forms().create(form).execute();
            Log.info("formbla = " + form);
        } catch (IOException e) {
            throw new ShopsException(ShopsErrors.GENERATION_ERROR, "Erreur lors de createNewForm", e);
        }
        formulaire.setFormId(form.getFormId());

        if (!driveService.publishForm(form.getFormId())) {
            throw new ShopsException(ShopsErrors.INVALID_PUBLISH_MESSAGE);
        }

        updateFormInfo(formulaire);
        addItemsToForm(formulaire);
        transformInQuiz(form.getFormId());

        formulaire.initNewFormulaire();

        formulaireRepository.save(formulaire);

        long endTime = System.currentTimeMillis();
        String executed = DurationFormatUtils.formatDurationHMS(endTime - startTime);
        Log.debug("Exécuté en : " + executed);
        return formulaire;
    }

    public Form getForm(String formId) {
        try {
            return credentialService.getFormsService().forms().get(formId).execute();
        } catch (IOException e) {
            throw new ShopsException(ShopsErrors.FORMULAIRE_NOT_FOUND, "Erreur lors de getForm", e);
        }
    }

    public String getFormAsPrettyString(String formId) {
        try {
            return getForm(formId).toPrettyString();
        } catch (IOException e) {
            throw new ShopsException(ShopsErrors.FORMULAIRE_NOT_FOUND, "Erreur lors de getFormAsPrettyString", e);
        }
    }

    public List<Formulaire> getFormulaires(List<Formulaire> formulaires) {
        List<Formulaire> lFormulaires = new ArrayList<>();
        // Récupération des items et de l'état (done) de chaque formulaire
        for (Formulaire formulaire : formulaires) {
            // Récupération du formulaire en base.
            // FIXME vérifier l'état done du formulaire
            Formulaire formulaireFind = formulaireRepository.getFormulaireByFormId(formulaire.getFormId());
            if (formulaireFind != null) {
                Log.info("found formulaire = " + formulaire.getFormId());
                lFormulaires.add(formulaireFind);
            } else {
                // Le formulaire n'a pas été trouvé en base. On va alors en fabriquer un et
                // l'enregistrer en base.
                Form form = getForm(formulaire.getFormId());
                TypeFormulaire defaulTypeFormulaire;
                try {
                    defaulTypeFormulaire = typeFormulaireRepository.getAllDefaultTypeFormulaire().stream()
                            .filter(dtf -> form.getInfo().getTitle().contains(dtf.getType()))
                            .findFirst()
                            .orElseGet(() -> typeFormulaireRepository.findById(1L).orElseThrow());
                } catch (Exception e) {
                    throw new ShopsException(ShopsErrors.SQL_ERROR, "Le type de formulaire n'a pas été trouvé", e);
                }

                TypeFormulaire typeFormulaire = new TypeFormulaire(
                        false,
                        defaulTypeFormulaire.getType(),
                        FormItemUtils.getFromForm(defaulTypeFormulaire, form));

                formulaire.setTypeFormulaire(typeFormulaire);
                formulaireRepository.save(formulaire);
                lFormulaires.add(formulaire);
            }
        }
        return lFormulaires;
    }

    // TODOS
    public String getResponses(String formId) {
        try {
            PermissionList list = credentialService.getDriveService().permissions().list(formId).execute();

            list.getPermissions().stream().forEach(perm -> {
                try {

                    // TODO voir à trouver permissions pour savoir si actif ou pas

                    Log.info("perm = " + perm.toPrettyString());
                    Log.info("getRole = " + perm.getRole());
                    // Log.info("getTeamDrivePermissionDetails = "
                    // +perm.getTeamDrivePermissionDetails().stream().toString());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });

            if (list.getPermissions().stream().filter(perm -> perm.getRole().equals(READER_ROLE)).findAny().isEmpty()) {
                Permission body = new Permission();
                body.setRole(READER_ROLE);
                body.setType("anyone");
                credentialService.getDriveService().permissions().create(formId, body).execute();
            }

            ListFormResponsesResponse responses = credentialService.getFormsService().forms().responses()
                    .list(formId)
                    .execute();
            // responses.getResponses().getFirst().getRespondentEmail()

            return responses.toPrettyString();
        } catch (IOException e) {
            throw new ShopsException(ShopsErrors.RESPONSE_ERROR, "Erreur lors de getResponses", e);
        }
    }

    public String desactiveFormResponse(String formId) {
        // TODO faire ça

        // credentialService.getFormsService().forms().responses().list(formId).

        // Form test =
        // credentialService.getFormsService().forms().get(formId).execute();
        // test.getSettings().;

        // BatchUpdateFormRequest batchRequest = new BatchUpdateFormRequest();
        // Request request = new Request();

        // FormSettings formSettings = new FormSettings();
        // formSettings.getQuizSettings().

        // request.setUpdateSettings(new UpdateSettingsRequest());
        // request.getUpdateSettings().setSettings(new FormSettings());

        // batchRequest.setRequests(Collections.singletonList(request));

        // credentialService.getFormsService().forms().batchUpdate(formId,
        // batchRequest).execute();
        return "OK";
    }
}
