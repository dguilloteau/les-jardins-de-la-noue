package org.dg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dg.dto.FormItem;
import org.dg.dto.FormItemId;
import org.dg.dto.TypeFormulaire;
import org.dg.dto.items.FImage;
import org.dg.dto.items.FList;
import org.dg.dto.items.FListElement;
import org.dg.dto.items.FQuestion;
import org.dg.dto.items.FText;
import org.dg.errors.ShopsErrors;
import org.dg.errors.ShopsException;

import com.google.api.services.forms.v1.model.ChoiceQuestion;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.forms.v1.model.Item;

import io.quarkus.logging.Log;

public class FormItemUtils {

    FormItemUtils() {
    }

    /**
     * Retourne la liste des FormItem en fonction du Form
     * 
     * @param defaulTypeFormulaire
     * @param form
     * @return
     */
    public static List<FormItem> getFromForm(TypeFormulaire defaulTypeFormulaire, Form form) {
        Log.debug("form = " + form);
        List<FormItem> formItems = new ArrayList<>();

        formItems.add(getFormItemEntete(defaulTypeFormulaire, form));

        form.getItems().stream().forEach(item -> {
            // C'est un ImageItem
            if (item.getImageItem() != null) {
                formItems.add(getFImage(defaulTypeFormulaire, item));
            }
            // C'est un TextItem
            if (item.getTextItem() != null) {
                formItems.add(getFText(defaulTypeFormulaire, item));
            }
            // C'est un QuestionItem
            if (item.getQuestionItem() != null) {
                // C'est une FList
                if (item.getQuestionItem().getQuestion().getTextQuestion() == null) {
                    formItems.add(getFList(defaulTypeFormulaire, item));
                } else {
                    formItems.add(getFQuestion(defaulTypeFormulaire, item));
                }
            }
        });

        return formItems;
    }

    private static FText getFormItemEntete(TypeFormulaire defaulTypeFormulaire, Form form) {
        Optional<FormItem> optDefaultFI = defaulTypeFormulaire.getFormItem(FormItemId.ENTETE);
        if (optDefaultFI.isPresent()) {
            FormItem defaultFormItem = optDefaultFI.get();
            if (form.getInfo() != null && !form.getInfo().getTitle().isEmpty()) {
                return new FText(defaultFormItem.getItemId(), defaultFormItem.getName(), defaultFormItem.getLibelle(),
                        defaultFormItem.isChecked(), form.getInfo().getTitle(), form.getInfo().getDescription());
            } else {
                return (FText) defaultFormItem;
            }
        } else {
            throw new ShopsException(ShopsErrors.GENERATION_ERROR, getDetailError(FormItemId.ENTETE.name()));
        }
    }

    private static FImage getFImage(TypeFormulaire defaulTypeFormulaire, Item item) {
        Log.debug("getFImage Id = " + item.getItemId());
        Log.debug("getFImage Title = " + item.getTitle());
        Log.debug("getFImage Content Uri = " + item.getImageItem().getImage().getContentUri());
        // Test de l'id, sinon, l'image bandeau n'a pas de titre
        if (FormItemId.IMAGE_BANDEAU.getId().equals(item.getItemId()) || item.getTitle() == null) {
            return getFormItemImage(defaulTypeFormulaire, item, FormItemId.IMAGE_BANDEAU);
        } else if (FormItemId.LOCALISATION.getId().equals(item.getItemId())
                || item.getTitle().toUpperCase().contains("LOCALISATION")) {
            return getFormItemImage(defaulTypeFormulaire, item, FormItemId.LOCALISATION);
        } else {
            return getFormItemImage(defaulTypeFormulaire, item, FormItemId.AUTRE);
        }
    }

    private static FText getFText(TypeFormulaire defaulTypeFormulaire, Item item) {
        Log.debug("getFText Id = " + item.getItemId());
        Log.debug("getFText Title = " + item.getTitle());
        Log.debug("getFText Description = " + item.getDescription());
        if (FormItemId.COMPOSITION.getId().equals(item.getItemId())
                || item.getTitle().toUpperCase().contains("COMPOSEZ")) {
            return getFormItemText(defaulTypeFormulaire, item, FormItemId.COMPOSITION);
        } else {
            return getFormItemText(defaulTypeFormulaire, item, FormItemId.AUTRE);
        }
    }

    private static FList getFList(TypeFormulaire defaulTypeFormulaire, Item item) {
        Log.debug("getFList Id = " + item.getItemId());
        Log.debug("getFList Title = " + item.getTitle());
        Log.debug("getFList Question = " + item.getQuestionItem().getQuestion());

        if (FormItemId.PANIER.getId().equals(item.getItemId())
                || item.getTitle().toUpperCase().contains("PANIER")) {
            return getFormItemList(defaulTypeFormulaire, item, FormItemId.PANIER);
        } else if (FormItemId.CAGETTE.getId().equals(item.getItemId())
                || item.getTitle().toUpperCase().contains("CAGETTE")) {
            return getFormItemList(defaulTypeFormulaire, item, FormItemId.CAGETTE);
        } else if (FormItemId.FORMULE.getId().equals(item.getItemId())
                || item.getTitle().toUpperCase().contains("FORMULE")) {
            return getFormItemList(defaulTypeFormulaire, item, FormItemId.FORMULE);
        } else {
            return getFormItemList(defaulTypeFormulaire, item, FormItemId.AUTRE);
        }
    }

    private static FQuestion getFQuestion(TypeFormulaire defaulTypeFormulaire, Item item) {
        Log.debug("getFQuestion Id = " + item.getItemId());
        Log.debug("getFQuestion Title = " + item.getTitle());
        // C'est une FQuestion
        if (FormItemId.E_MAIL.getId().equals(item.getItemId())
                || item.getTitle().toUpperCase().contains("MAIL")) {
            return getFormItemQuestion(defaulTypeFormulaire, item, FormItemId.E_MAIL);
        } else if (FormItemId.NOM_PRENOM.getId().equals(item.getItemId())
                || item.getTitle().toUpperCase().contains("NOM")) {
            return getFormItemQuestion(defaulTypeFormulaire, item, FormItemId.NOM_PRENOM);
        } else {
            return getFormItemQuestion(defaulTypeFormulaire, item, FormItemId.AUTRE);
        }
    }

    private static FImage getFormItemImage(TypeFormulaire defaulTypeFormulaire, Item item, FormItemId formItemId) {
        Optional<FormItem> optDefaultFI = defaulTypeFormulaire.getFormItem(formItemId);
        if (optDefaultFI.isPresent()) {
            FormItem defaultFormItem = optDefaultFI.get();
            return new FImage(defaultFormItem.getItemId(), defaultFormItem.getName(), defaultFormItem.getLibelle(),
                    defaultFormItem.isChecked(), item.getTitle(), item.getImageItem().getImage().getContentUri());
        } else {
            throw new ShopsException(ShopsErrors.GENERATION_ERROR, getDetailError(formItemId.name()));
        }
    }

    private static FText getFormItemText(TypeFormulaire defaulTypeFormulaire, Item item, FormItemId formItemId) {
        Optional<FormItem> optDefaultFI = defaulTypeFormulaire.getFormItem(formItemId);
        if (optDefaultFI.isPresent()) {
            FormItem defaultFormItem = optDefaultFI.get();
            return new FText(defaultFormItem.getItemId(), defaultFormItem.getName(), defaultFormItem.getLibelle(),
                    defaultFormItem.isChecked(), item.getTitle(), item.getDescription());
        } else {
            throw new ShopsException(ShopsErrors.GENERATION_ERROR, getDetailError(formItemId.name()));
        }
    }

    private static FList getFormItemList(TypeFormulaire defaulTypeFormulaire, Item item, FormItemId formItemId) {
        Optional<FormItem> optDefaultFI = defaulTypeFormulaire.getFormItem(formItemId);
        if (optDefaultFI.isPresent()) {
            FormItem defaultFormItem = optDefaultFI.get();
            ChoiceQuestion choiceQuestion = item.getQuestionItem().getQuestion().getChoiceQuestion();
            FList fList = new FList(defaultFormItem.getItemId(), defaultFormItem.getName(),
                    defaultFormItem.getLibelle(),
                    defaultFormItem.isChecked(), item.getTitle(),
                    choiceQuestion.getType());

            choiceQuestion.getOptions().stream().forEach(opt -> {
                Log.debug("Value = " + opt.getValue());
                if (opt.getImage() != null) {
                    Log.debug("ContentUri = " + opt.getImage().getContentUri());
                    fList.addFListToFListElement(new FListElement(opt.getImage().getContentUri(), opt.getValue()));
                } else {
                    fList.addFListToFListElement(new FListElement(null, opt.getValue()));
                }
            });

            return fList;
        } else {
            throw new ShopsException(ShopsErrors.GENERATION_ERROR, getDetailError(formItemId.name()));
        }
    }

    private static FQuestion getFormItemQuestion(TypeFormulaire defaulTypeFormulaire, Item item,
            FormItemId formItemId) {
        Optional<FormItem> optDefaultFI = defaulTypeFormulaire.getFormItem(formItemId);
        if (optDefaultFI.isPresent()) {
            FormItem defaultFormItem = optDefaultFI.get();
            return new FQuestion(defaultFormItem.getItemId(), defaultFormItem.getName(), defaultFormItem.getLibelle(),
                    defaultFormItem.isChecked(), item.getTitle());
        } else {
            throw new ShopsException(ShopsErrors.GENERATION_ERROR, getDetailError(formItemId.name()));
        }
    }

    private static String getDetailError(String name) {
        return "Pas de FormItem " + name + " par défaut";
    }
}
