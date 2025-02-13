package com.dongnv.paygent.paymentGateways;

import com.dongnv.paygent.common.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.ks.merchanttool.connectmodule.system.PaygentB2BModule;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Controller
@CrossOrigin
@RequestMapping("/api")
public class ApiPaygentResponseController {

    private String mid = "39499";
    private String tokenKey = "test_VfBJ9HIUiHcpVceytpDGSiSV";
    private String url = "https://sandbox.paygent.co.jp/n/token/request";
    private String connecId = "test39499";
    private String connecPw = "eNFpmbSaGjn";
    private String userId = "1234567";
    private String moneyNo = "20250212132738345";

    @GetMapping(value = "/paygent/response_term_url")
    public String responseTermUrl(
            @RequestParam(value = "issur_class", required = false) String issurClass,
            @RequestParam(value = "acq_id", required = false) String acqId,
            @RequestParam(value = "acq_name", required = false) String acqName,
            @RequestParam(value = "masked_card_number", required = false) String maskedCardNumber,
            @RequestParam(value = "result", required = false) String result,
            @RequestParam(value = "issur_name", required = false) String issurName,
            @RequestParam(value = "3ds_auth_id", required = false) String threeDsAuthId,
            @RequestParam(value = "3dsecure_requestor_error_code", required = false) String threeDsRequestorErrorCode,
            @RequestParam(value = "card_brand", required = false) String cardBrand,
            @RequestParam(value = "fingerprint", required = false) String fingerprint,
            @RequestParam(value = "attempt_kbn", required = false) String attemptKbn,
            @RequestParam(value = "3dsecure_server_error_code", required = false) String threeDsServerErrorCode,
            @RequestParam(value = "hc", required = false) String hc,
            @RequestParam(value = "issur_id", required = false) String issurId,
            Model model
    ) {
        try {
            MultiValueMap<String, String> postData = new LinkedMultiValueMap<>();
            postData.add("merchant_id", mid);
            postData.add("token_generate_key", tokenKey);
            postData.add("card_number", "4566000000000000");
            postData.add("card_name", "BAVYA");
            postData.add("card_expire_year", "29");
            postData.add("card_expire_month", "12");
            postData.add("card_cvc", "111");

            String cardToken = getToken(postData, url);

            PaygentB2BModule module = new PaygentB2BModule();
            module.reqPut("telegram_kind", "020");
            module.reqPut("telegram_version", "1.0");
            module.reqPut("merchant_id", mid);
            module.reqPut("trading_id", moneyNo.replaceAll("-", ""));
            module.reqPut("connect_id", connecId);
            module.reqPut("connect_password", connecPw);
            module.reqPut("payment_amount", "314010");
            module.reqPut("payment_class", "10");

            module.reqPut("3dsecure_use_type", "2");
            module.reqPut("card_token", cardToken);
            module.reqPut("3ds_auth_id", threeDsAuthId);

            module.setSendFilePath("");
            module.post();
            String orderId = "1233333333333333333333";
            if (!module.hasResNext() || !"0".equals(module.getResultStatus())) {
                model.addAttribute("retCode", "ERROR");
                orderId = null;
            }
            if(StringUtil.isNotNullAndNotEmpty(orderId)) {
                model.addAttribute("orderId", orderId);
            }

            return "popup/popup_response_payment";
        } catch (Exception ex) {
            return "popup/popup_response_payment";
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
        return (String)tokenizedCardObject.get("token");
    }
}
