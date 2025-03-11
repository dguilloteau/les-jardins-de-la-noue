// package org.dg;

// import static io.restassured.RestAssured.given;
// import static org.hamcrest.CoreMatchers.is;

// import org.dg.errors.ShopsErrors;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.ValueSource;

// import io.quarkus.test.junit.QuarkusTest;
// import io.restassured.http.ContentType;

// @QuarkusTest
// class TypesFormulairesControllerTest {

//     @Test
//     void getAllFormsTypes() {
//         String getAllFormsTypesPath = "src/test/resources/getAllFormsTypes.json";

//         given()
//                 .contentType(ContentType.JSON)
//                 .when().get("/formulaire/types/all")
//                 .then()
//                 .assertThat()
//                 .contentType(ContentType.JSON)
//                 .statusCode(200)
//                 .body(is(Utils.getJsonFromFile(getAllFormsTypesPath)));
//     }

//     @ParameterizedTest
//     @ValueSource(longs = { 1, 2, 3 })
//     void getFormType(Long id) {
//         String getFormTypePath = "src/test/resources/getFormType" + id + ".json";

//         given()
//                 .contentType(ContentType.JSON)
//                 .when().get("/formulaire/types/" + id)
//                 .then()
//                 .assertThat()
//                 .contentType(ContentType.JSON)
//                 .statusCode(200)
//                 .body(is(Utils.getJsonFromFile(getFormTypePath)));
//     }

//     @Test
//     void patchFormType() {
//         String json = "{\"titre\":\"Adresse e-mail\",\"id\":3,\"itemId\":\"00000003\",\"name\":\"E_MAIL\",\"libelle\":\"E-mail\",\"checked\":true}";

//         given()
//                 .contentType(ContentType.JSON)
//                 .body(json)
//                 .when()
//                 .patch("/formulaire/types/patch")
//                 .then()
//                 .assertThat()
//                 .contentType(ContentType.JSON)
//                 .statusCode(200)
//                 .body(is("OK"));
//     }

//     @Test
//     void getFormTypeThrow() {

//         given()
//                 .contentType(ContentType.JSON)
//                 .when().get("/formulaire/types/" + 0)
//                 .then()
//                 .assertThat()
//                 .contentType(ContentType.JSON)
//                 .statusCode(ShopsErrors.SQL_ERROR.getStatus().getStatusCode())
//                 .body(is(ShopsErrors.SQL_ERROR.getDescription()));
//     }

// }