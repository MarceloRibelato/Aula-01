package steps;

import api.ApiParams;
import api.ApiRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import utils.PropertiesUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CorreiosSteps extends ApiRequest {

    PropertiesUtils prop = new PropertiesUtils();
    ApiParams apiParams = new ApiParams();

    @Dado("que possou um token valido")
    public void que_possou_um_token_valido() {
        System.out.println("Api não requer token!");
    }

    @Quando("envio um request com dados validos")
    public void envio_um_request_com_dados_validos() {
        super.uri = prop.getProp("uri_correios");
        super.params = apiParams.correiosParams();
        super.GET();
    }

    @Entao("o valor do frete deve ser calculado")
    public void o_valor_do_frete_deve_ser_calculado() {
        assertTrue(Float.parseFloat(response.xmlPath().getString("Servicos.cServico.Valor")
                .replace(",", ".")) > 0);
    }

    @E("o status code deve ser {int}")
    public void oStatusCodeDeveSer(Integer statusCodeEsperado) {
        assertEquals(statusCodeEsperado, response.statusCode());
    }

    @Quando("envio um request com dados validos datatable")
    public void envioUmRequestComDadosValidosDatatable(DataTable dataTable) {
        super.uri = prop.getProp("uri_correios");
        super.params = apiParams.setParms(dataTable.asMaps().get(0));
        super.GET();
    }

    @Entao("o valor do frete deve ser {string}")
    public void oValorDoFreteDeveSer(String valorEsperado) {
        assertEquals(valorEsperado, response.xmlPath().getString("Servicos.cServico.Valor"));
    }

    @Quando("envio um request com dados {string}, {string}")
    public void envioUmRequestComDados(String cepOrigem, String cepDestino) {
        super.uri = prop.getProp("uri_correios");
        super.params = apiParams.correiosParams();
        super.params.put("sCepOrigem", cepOrigem);
        super.params.put("sCepDestino", cepDestino);
        super.GET();
    }

    @Entao("deve ser exibida a mensagem {string}")
    public void deveSerExibidaAMensagem(String msgEsperada) {
        assertEquals(msgEsperada, response.xmlPath().getString("Servicos.cServico.MsgErro"));
    }
}
