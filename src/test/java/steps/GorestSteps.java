package steps;

import api.ApiHeaders;
import api.ApiRequest;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.json.JSONException;
import org.json.JSONObject;
import user.UsersLombok;
import utils.PropertiesUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GorestSteps extends ApiRequest {

    PropertiesUtils prop = new PropertiesUtils();
    ApiHeaders apiHeaders = new ApiHeaders();
    Faker faker = new Faker();
    UsersLombok userEnvio;

    @Dado("que possou gorest token valido")
    public void que_possou_gorest_token_valido() {
        token = prop.getProp("token_gorest");
    }

    @Quando("envio um request de cadastro de usuario com dados validos")
    public void envio_um_request_de_cadastro_de_usuario_com_dados_validos() throws JSONException {
        super.uri = prop.getProp("uri_gorest");
        super.headers = apiHeaders.gorestHeaders(token);
        userEnvio = UsersLombok.builder()
                .email(faker.internet().emailAddress())
                .name(faker.name().fullName())
                .gender("female")
                .status("active")
                .build();

        super.body = new JSONObject(new Gson().toJson(userEnvio));
        super.POST();
    }


    @Entao("o usuario deve ser criado corretamente")
    public void o_usuario_deve_ser_criado_corretamente() throws JSONException {
        assertEquals(userEnvio, response.jsonPath().getObject("data", UsersLombok.class),
                "Erro  na comparação do objeto!");
    }

    @Entao("o status code do request deve ser {int}")
    public void o_status_code_do_request_deve_ser(Integer statusEsperado) {
        assertEquals(statusEsperado, response.statusCode(), "Status code diferente do esperado!");
    }

    @E("existe um usuario cadastrado na api")
    public void existeUmUsuarioCadastradoNaApi() {
        envio_um_request_de_cadastro_de_usuario_com_dados_validos();
    }

    @Quando("buscar esse usuario")
    public void buscarEsseUsuario() {
        super.uri = prop.getProp("uri_gorest") + "/" + response.jsonPath().getJsonObject("data.id");
        super.headers = apiHeaders.gorestHeaders(token);
        super.body = new JSONObject();
        super.GET();
    }

    @Entao("os dados dos usuario devem ser retornados")
    public void osDadosDosUsuarioDevemSerRetornados() {
        assertEquals(userEnvio, response.jsonPath().getObject("data", UsersLombok.class),
                "Erro  na comparação do objeto!");
    }

    @Quando("altero os dados do usuario")
    public void alteroOsDadosDoUsuario() {
        super.uri = prop.getProp("uri_gorest") + "/" + response.jsonPath().getJsonObject("data.id");
        super.headers = apiHeaders.gorestHeaders(token);
        userEnvio.setStatus("inactive");
        super.body = new JSONObject(new Gson().toJson(userEnvio));
        super.PUT();
    }

    @Entao("o usuario deve ser alterado com sucesso")
    public void oUsuarioDeveSerAlteradoComSucesso() {
        assertEquals(userEnvio, response.jsonPath().getObject("data", UsersLombok.class),
                "Erro  na comparação do objeto!");
    }

    @Quando("altero um ou mais dados do usuario")
    public void alteroUmOuMaisDadosDoUsuario() {
        super.uri = prop.getProp("uri_gorest") + "/" + response.jsonPath().getJsonObject("data.id");
        super.headers = apiHeaders.gorestHeaders(token);
        userEnvio.setGender("male");
        super.body = new JSONObject("{\"gender\":\"male\"}");
        super.PATCH();
    }

    @Quando("deleto esse usuario")
    public void deletoEsseUsuario() {
        super.uri = prop.getProp("uri_gorest") + "/" + response.jsonPath().getJsonObject("data.id");
        super.headers = apiHeaders.gorestHeaders(token);
        super.body = new JSONObject();
        super.DELETE();
    }

    @Entao("o usuario é deletado corretamente")
    public void oUsuarioÉDeletadoCorretamente() {
        assertEquals("", response.asString());
    }
}
