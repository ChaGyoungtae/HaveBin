package com.HaveBinProject.HaveBin.MailAuth;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
@Service
@PropertySource("classpath:application.yml")
public class MailService {

    private final JavaMailSender emailSender;

    public static String ePw = "";

    private MimeMessage createMessage(String to) throws Exception {
        System.out.println("보내는 대상 : " + to);
        System.out.println("인증 번호 : " + ePw);

        // HTML 형식의 이메일 메시지
        String msgg =
                        "<div style='width: 700px; overflow: hidden; padding-left: 20px'>\n" +
                        "   <div style='margin-bottom: 30px'>\n" +
                        "       <div style='color: #000; font-size: 34px; font-weight: 700; line-height: normal; letter-spacing: 0.085px;'>\n" +
                        "            <img src='https://havebin.s3.ap-northeast-2.amazonaws.com/image/logo.svg' width='120px' height='80px'>\n" +
                        "       </div>\n" +
                        "       <div style='color: gray; font-weight: 700; line-height: normal; letter-spacing: 0.085px;'>\n" +
                        "        안녕하세요, 요청하신 인증번호를 보내드립니다.\n" +
                        "       </div>\n" +
                        "       <div style='margin-top: 10px; color: gray; font-weight: 700; line-height: normal; letter-spacing: 0.04px;'>\n" +
                        "           3분 이내에 이메일에 있는 인증코드를 입력해 주세요.\n" +
                        "       </div>\n" +
                        "       <div style=' margin-top: 46px; color: #000; font-size: 36px; font-weight: 700;'>\n" +
                        "            " + ePw + "\n" +
                        "       </div>\n" +
                        "       <div style='margin-top: 46px; color: rgb(182, 178, 178); font-weight: 700; font-weight: 700; width: 700px; font-size: 9pt;'>\n" +
                        "        본인이 인증번호를 요청하지 않았을 경우, 본 이메일을 무시해주세요.\n" +
                        "       </div>\n" +
                        "   </div>\n" +
                        "</div>\n" +
                        "<div style='background-color: #97C751; width: 680px; color: #FFFFFF; padding-left: 20px; padding-top: 10px; padding-bottom: 10px;'>\n" +
                        "   <div style='margin-bottom: 10px; font-size:8pt; text-decoration: none;'>Homepage: <a href=\"https://www.have-bin.com\" style=\"text-decoration: none; color: #FFFFFF;\">https://www.have-bin.com</a></div>\n" +
                        "   <div style='margin-bottom: 30px; font-size:8pt'>Mail: have-bin@naver.com</div>\n" +
                        "   <div style='margin-bottom: 10px; font-size:8pt'>ⓒ 2024. [Have-bin] all rights reserved</div>\n" +
                        "   <div style='margin-bottom: 8px; font-size: 6pt;'>이 자료는 대한민국 저작권법의 보호를 받습니다.</div>\n" +
                        "   <div style='font-size: 6pt;'>작성된 모든 내용의 권리는 작성자에게 있으며, 작성자의 동의 없는 사용이 금지됩니다.</div>\n" +
                        "</div>";

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // 보내는 대상
        message.setSubject("[여따버려] 회원가입 이메일 인증번호"); // 제목

        // HTML 형식의 메시지를 설정합니다.
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("have-bin@naver.com", "여따버려[HAVEBIN]"));

        return message;
    }


    public static String createKey(int length) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        Set<Integer> usedNumbers = new HashSet<>();

        while (code.length() < length) {
            SimpleDateFormat format1 = new SimpleDateFormat ( "ss");
            Date time = new Date();
            int time1 = Integer.parseInt(format1.format(time));

            int randomNumber = random.nextInt(10) * time1;// 0부터 9 사이의 랜덤 숫자 생성

            if (randomNumber >= 10) {
                randomNumber = randomNumber % (randomNumber / 10);
            }

            // 생성된 숫자가 중복되지 않도록 확인
            if (!usedNumbers.contains(randomNumber)) {
                code.append(randomNumber);
                usedNumbers.add(randomNumber);
            }
        }

        return code.toString();
    }

    public String sendSimpleMessage(String to)throws Exception {
        ePw= createKey(4);

        // TODO Auto-generated method stub
        MimeMessage message = createMessage(to);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }

        return ePw;
    }

}
