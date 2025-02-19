// package org.dg;

// import static io.restassured.RestAssured.get;
// import static io.restassured.RestAssured.given;
// import static io.restassured.RestAssured.post;
// import static org.hamcrest.CoreMatchers.is;
// import static org.hamcrest.CoreMatchers.isA;

// import java.net.URI;

// import org.dg.dto.Formulaire;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.ValueSource;

// import com.github.tomakehurst.wiremock.client.WireMock;

// import io.quarkiverse.wiremock.devservice.ConnectWireMock;
// import io.quarkus.test.common.QuarkusTestResource;
// import io.quarkus.test.junit.QuarkusTest;
// import io.restassured.http.ContentType;

// @QuarkusTest
// @ConnectWireMock
// @QuarkusTestResource(WiremockTestResource.class)
// class FormulairesControllerTest {

//     WireMock wiremock;

//     @ParameterizedTest
//     // @ValueSource(strings = { "Ferme", "CNRS", "DPGD" })
//     @ValueSource(strings = { "Ferme" })
//     void postFormulaire(String type) {
//         String postFormulairePath = "src/test/resources/postFormulaire" + type + ".json";
//         String postFormulaireResponsePath = "src/test/resources/postFormulaire" + type + "Response.json";

//         Assertions.assertNotNull(wiremock);
//         wiremock.register(get(new URI("https://forms.googleapis.com/v1/forms"))
//                 .willReturn(WireMock.aResponse().withStatus(200).withBody("MOCK_MSG")));

//         // Formulaire formulaire = new
//         // Formulaire().fromJsonString(Utils.getJsonFromFile(postFormulairePath));
//         // Formulaire newFormulaire = new
//         // Formulaire().fromJsonString(Utils.getJsonFromFile(postFormulaireResponsePath));

//         given()
//                 .contentType(ContentType.JSON)
//                 .body(Utils.getJsonFromFile(postFormulairePath))
//                 .when().post("/formulaire/create")
//                 .then()
//                 .assertThat()
//                 .contentType(ContentType.JSON)
//                 .statusCode(200)
//                 .body(is(Utils.getJsonFromFile(postFormulaireResponsePath)));
//     }

// }
