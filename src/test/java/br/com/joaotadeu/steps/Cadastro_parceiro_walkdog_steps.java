package br.com.joaotadeu.steps;

import io.cucumber.java.After;

import io.cucumber.java.Before;

import io.cucumber.java.Scenario;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class Cadastro_parceiro_walkdog_steps {

    //Transforma a Variável e uma Variável global
    private WebDriver navegador;

    @Before
    public void setUp(){
        //Configuração do navegador
        navegador = new FirefoxDriver();
        WebDriverManager.firefoxdriver().setup();
        navegador.manage().window().maximize();
        System.out.println("Iniciando Teste...");

    }

    @After(order = 1)
    public void tirarPrint(Scenario cenario) throws IOException {
      File file =  ((TakesScreenshot)navegador).getScreenshotAs(OutputType.FILE);
      FileUtils.copyFile(file, new File("targer/screenshot/"+cenario.getId()+".jpg"));
    }

    @After(order = 0)
    public void fecharNavegador(){
        navegador.quit();
        System.out.println("Finalizando Teste...");

    }

    @Dado("que estou na pagina principal do WalkDog")
    public void que_estou_na_pagina_principal_do_walk_dog() {
        navegador.get("https://walkdog.vercel.app/");
        navegador.findElement(By.cssSelector(".content > main:nth-child(1) > a:nth-child(4) > span:nth-child(1)")).click();

    }

    @Quando("preencho os dados pessoais do parceiro")
    public void preencho_os_dados_pessoais_do_parceiro(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> dadosParceiro = dataTable.asMaps(String.class, String.class);

        WebElement nomeInput = navegador.findElement(By.name("name"));
        WebElement emailInput = navegador.findElement(By.name("email"));
        WebElement cpfInput = navegador.findElement(By.name("cpf"));
        WebElement cepInput = navegador.findElement(By.name("cep"));
        WebElement numeroInput = navegador.findElement(By.name("addressNumber"));
        WebElement complementoInput = navegador.findElement(By.name("addressDetails"));

        // Preenchendo os campos com os dados do parceiro
        nomeInput.sendKeys(dadosParceiro.get(0).get("Nome Completo"));
        emailInput.sendKeys(dadosParceiro.get(0).get("E-mail"));
        cpfInput.sendKeys(dadosParceiro.get(0).get("CPF"));
        cepInput.sendKeys(dadosParceiro.get(0).get("CEP"));
        navegador.findElement(By.cssSelector("input[type=\"button\"]")).click();
        numeroInput.sendKeys(dadosParceiro.get(0).get("Numero"));
        complementoInput.sendKeys(dadosParceiro.get(0).get("Complemento"));

    }

    @Quando("escolho atividade extra")
    public void escolho_atividade_extra(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> atividadesExtras = dataTable.asMaps(String.class, String.class);

        // Verificar se há atividades extras especificadas
        if (!atividadesExtras.isEmpty()) {
            String atividadeExtra = atividadesExtras.get(0).get("Atividade Extra");

            // Verificando o tipo de atividade e realizando a seleção correspondente
            switch (atividadeExtra.toLowerCase()) {
                case "cuidar":
                    // Lógica para selecionar a atividade "cuidar"
                    navegador.findElement(By.cssSelector(".walker-service > li:nth-child(1) > span:nth-child(2)")).click();
                    break;
                case "adestrar":
                    // Lógica para selecionar a atividade "adestrar"
                    navegador.findElement(By.cssSelector(".walker-service > li:nth-child(2) > span:nth-child(2)")).click();
                    break;
                default:
                    // Tratamento para tipos de atividade inválidos
                    System.out.println("Tipo de atividade extra inválido: " + atividadeExtra);
                    break;
            }
        } else {
            System.out.println("Nenhuma atividade extra especificada.");
        }

    }

    @Quando("faço upload do documento de verificação do parceiro")
    public void faço_upload_do_documento_de_verificação_do_parceiro(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> dadosDocumento = dataTable.asMaps(String.class, String.class);

        // Obter o caminho do arquivo da DataTable
        String caminhoDoArquivo = dadosDocumento.get(0).get("Caminho Documento");

        // Localizar o elemento de upload na página
        WebElement fileInput = navegador.findElement(By.cssSelector("input[type='file']"));

        // Enviar o caminho do arquivo para o elemento de upload
        fileInput.sendKeys(caminhoDoArquivo);

        navegador.findElement(By.cssSelector(".button-register")).click();

    }

    @Então("devo ver a mensagem {string}")
    public void devoVerAMensagem(String mensagemEsperada) {
        // Tempo limite de espera em segundos
        int tempoEsperaEmSegundos = 2;

        // Converte o tempo de espera em segundos para um objeto Duration
        Duration tempoDeEspera = Duration.ofSeconds(tempoEsperaEmSegundos);

        // Aguardar até que o elemento seja visível
        WebDriverWait wait = new WebDriverWait(navegador, tempoDeEspera);
        WebElement mensagemElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#swal2-html-container")));

        // Verificar se a mensagem exibida é igual à mensagem esperada
        String mensagemExibida = mensagemElement.getText();
        Assert.assertEquals(mensagemEsperada, mensagemExibida);

    }

    @Quando("tento cadastrar um parceiro sem preencher os campos obrigatórios")
    public void tentoCadastrarUmParceiroSemPreencherOsCamposObrigatorios() {
        navegador.findElement(By.cssSelector(".button-register")).click();

    }

    @Então("devo ver uma mensagem de erro informando que os campos obrigatórios não foram preenchidos")
    public void devo_ver_uma_mensagem_de_erro_informando_que_os_campos_obrigatórios_não_foram_preenchidos(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> dados = dataTable.asMaps(String.class, String.class);

        // Encontrar e validar a mensagem de erro para o campo Nome Completo
        WebElement nomeErrorElement = navegador.findElement(By.cssSelector("#page-walker > form:nth-child(2) > fieldset:nth-child(3) > div:nth-child(2) > div:nth-child(1) > span:nth-child(2)"));
        String nomeErrorMessage = nomeErrorElement.getText();
        Assert.assertEquals(dados.get(0).get("Nome Completo"), nomeErrorMessage);

        // Encontrar e validar a mensagem de erro para o campo E-mail
        WebElement emailErrorElement = navegador.findElement(By.cssSelector("div.field-group:nth-child(3) > div:nth-child(1) > span:nth-child(2)"));
        String emailErrorMessage = emailErrorElement.getText();
        Assert.assertEquals(dados.get(0).get("E-mail"), emailErrorMessage);

        // Encontrar e validar a mensagem de erro para o campo CPF
        WebElement cpfErrorElement = navegador.findElement(By.cssSelector("div.field-group:nth-child(3) > div:nth-child(2) > span:nth-child(2)"));
        String cpfErrorMessage = cpfErrorElement.getText();
        Assert.assertEquals(dados.get(0).get("CPF"), cpfErrorMessage);

        // Encontrar e validar a mensagem de erro para o campo Número
        WebElement numeroErrorElement = navegador.findElement(By.cssSelector("div.field-group:nth-child(4) > div:nth-child(1) > span:nth-child(2)"));
        String numeroErrorMessage = numeroErrorElement.getText();
        Assert.assertEquals(dados.get(0).get("Número"), numeroErrorMessage);

        // Encontrar e validar a mensagem de erro para o campo Documento
        WebElement documentoErrorElement = navegador.findElement(By.cssSelector("span.alert-error:nth-child(7)"));
        String documentoErrorMessage = documentoErrorElement.getText();
        Assert.assertEquals(dados.get(0).get("Documento"), documentoErrorMessage);

    }

}
