package com.dongnv.paygent.paymentGateways;


import com.dongnv.paygent.common.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.ks.merchanttool.connectmodule.system.PaygentB2BModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ApiPaygentController {

    private String mid = "39499";
    private String tokenKey = "test_VfBJ9HIUiHcpVceytpDGSiSV";
    private String url = "https://sandbox.paygent.co.jp/n/token/request";
    private String connecId = "test39499";
    private String connecPw = "eNFpmbSaGjn";
    private String userId = "1234567";
    private String moneyNo = "20250212132738345";
    public static String term_url = "http://192.168.0.70:8090/api/paygent/response_term_url";

    @PostMapping("/pay")
    public ResponseEntity<?> processPayment(@RequestBody Map<String, Object> request) {
        Map<String, String> response = new HashMap<>();
        try {
            String cardNumber = (String) request.get("cardNumber");
            String cardName = (String) request.get("cardName");
            String expiryMonth = (String) request.get("expiryMonth");
            String expiryYear = (String) request.get("expiryYear");
            String cvv = (String) request.get("cvv");
            String amount = (String) request.get("amount");
            //String moneyNo = String.format("%012d", (int) (Math.random() * 1000000000000L));

            MultiValueMap<String, String> postData = new LinkedMultiValueMap<>();
            postData.add("merchant_id", mid);
            postData.add("token_generate_key", tokenKey);
            postData.add("card_number", cardNumber);
            postData.add("card_name", cardName);
            postData.add("card_expire_year", expiryYear);
            postData.add("card_expire_month", expiryMonth);
            postData.add("card_cvc", cvv);


            String resultToken = getToken(postData, url);
            String createCusStatus = ""; // 処理結果 0=正常終了, 1=異常終了
            boolean hasResNext = false;
            String cusId = "14363077";


            PaygentB2BModule createCus = new PaygentB2BModule();

//            if(cusId == null || cusId.isEmpty()){
//                createCus = this.createCusId(StringUtil.convertObjectToString(tokenizedCardObject.get("token")), userId);
//                createCusStatus = createCus.getResultStatus();
//                hasResNext = createCus.hasResNext();
//                if(!hasResNext || !createCusStatus.equals("0")) {
//                    String messageError =  createCus.getResponseDetail();
//                    System.err.println("Error "+  messageError);
//                }
//                cusId = (String)createCus.resNext().get("customer_card_id");
//            }

            PaygentB2BModule module3DSAuthen = request3DSAuthentication(moneyNo, amount, cusId);
            boolean has450Success = module3DSAuthen.hasResNext();
            boolean restCodeStatus = module3DSAuthen.getResultStatus().equals("0");
            if (!has450Success || !restCodeStatus) {
                String messageError = module3DSAuthen.getResponseDetail();
                System.err.println("Error " + messageError);
            }

            Map<String, Object> map = module3DSAuthen.resNext();
            String outAcsHtml = StringUtil.convertObjectToString(map.get("out_acs_html"));
//            System.out.println(outAcsHtml);
            String paymentId = (String) map.get("payment_id");
            response.put("status", "success");
            response.put("outAcsHtml", outAcsHtml);

            System.err.println("final");
            System.out.println("response : " + response.toString());
        } catch (Exception ex) {
            System.err.println("error " + ex.getMessage());
            response.put("status", "error");
            response.put("message", ex.getMessage());

        }
        return ResponseEntity.ok(response);

    }


    private PaygentB2BModule request3DSAuthentication(String moneyNo, String amount, String cusId) throws Exception {
        PaygentB2BModule module = new PaygentB2BModule();
        module.reqPut("telegram_kind", "450");
        module.reqPut("telegram_version", "1.0");
        module.reqPut("merchant_id", mid);
        module.reqPut("trading_id", moneyNo.replaceAll("-", ""));
        module.reqPut("connect_id", connecId);
        module.reqPut("connect_password", connecPw);
        module.reqPut("stock_card_mode", "1");
        module.reqPut("customer_id", userId);
        module.reqPut("customer_card_id", cusId);
        module.reqPut("payment_amount", amount);
        module.reqPut("payment_class", "10");
        module.reqPut("3dsecure_use_type", "2");
        module.reqPut("authentication_type", "01");
        module.reqPut("term_url", term_url);
        module.reqPut("card_set_method", "customer");
        module.reqPut("merchant_name", "direct");

        module.setSendFilePath("");
        module.post();
        return module;
    }


    public PaygentB2BModule createCusId(String cardToken, String cusId) {
        try {
            mid = "39499";//smWownetPg.getPgCardMid();
            connecId = "test39499";//smWownetPg.getPgBankMid();
            connecPw = "eNFpmbSaGjn";//smWownetPg.getPgBankKey();

            PaygentB2BModule module = new PaygentB2BModule();
            module.reqPut("telegram_kind", "025");  //025 Đăng ký thẻ
            module.reqPut("telegram_version", "1.0");
            module.reqPut("merchant_id", mid);
            module.reqPut("connect_id", connecId);
            module.reqPut("connect_password", connecPw);
            module.reqPut("stock_card_mode", "1");
            module.reqPut("customer_id", cusId);
            module.reqPut("payment_class", "10"); //10 thanh toán một lần
            module.reqPut("card_token", cardToken);
            module.setSendFilePath("");
            module.post();
            return module;

        } catch (Exception e) {
            System.err.println("createCusId " + e.getMessage());
            return null;
        }
    }


    public static String getToken(MultiValueMap<String, String> postData, String url) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept-Encoding", "EUC-KR");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(postData, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        responseEntity.getBody();

        Map<String, Object> resultToken = objectMapper.readValue(responseEntity.getBody().toString(), Map.class);
        if (!"0000".equals(StringUtil.convertObjectToString(resultToken.get("result")))) {
            System.err.println("Error " + resultToken.get("result"));
            return null;
        }
        Map<String, Object> tokenizedCardObject = (Map<String, Object>) resultToken.get("tokenizedCardObject");
        return (String) tokenizedCardObject.get("token");
    }
}
