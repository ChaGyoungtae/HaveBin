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

    private MimeMessage createMessage(String to) throws Exception{
        System.out.println("보내는 대상 : " + to);
        System.out.println("인증 번호 : " + ePw);
        // https://seumu-s3-bucket.s3.ap-northeast-2.amazonaws.com/banner_pattern.svg
        // https://drive.google.com/uc?export=view&amp;id=1Sq-EX7P31_gwDoSmUh5SJDD-8z1dfhKq

        String msgg =
                "<div style=\"width: 1280px; overflow: hidden;\">\n" +
                        "   <div>\n" +
                        "       <div style=\"color: #000; font-size: 34px; font-weight: 700; line-height: normal; letter-spacing: 0.085px; width: 700px;\">\n" +
                        "            여따버려\n" +
                        "       </div>\n" +
                        "       <div style=\"margin-top: 46px; color: #000; font-size: 16px; font-weight: 400; line-height: normal; letter-spacing: 0.04px; width: 100%;\">\n" +
                        "           3분 이내에 이메일에 있는 인증코드를 입력해 주세요.\n" +
                        "       </div>\n" +
                        "       <div style=\"margin-top: 46px; color: #000; font-size: 36px; font-weight: 700; line-height: normal; text-decoration: underline; width: 700px;\">\n" +
                        "            인증 코드: "+ePw+"\n" +
                        "       </div>\n" +
                        "   </div>\n" +
                        "</div>";

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);//보내는 대상
        message.setSubject("[여따버려] 회원가입 이메일 인증번호");//제목

        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("ks06891@naver.com","여따버려[HAVEBIN]"));

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
