package cn.wulin.brace.demo.tts;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TextToSpeechGoogle {

    public static void main(String[] args) {
        try {
            synthesizeTextToMp3("你好，我叫小峰。ok thank you!", "output.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void synthesizeTextToMp3(String text, String outputPath) throws IOException {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("cmn-CN")
                    .setSsmlGender(SsmlVoiceGender.MALE)
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioContents = response.getAudioContent();

            try (OutputStream outputStream = new FileOutputStream(outputPath)) {
                audioContents.writeTo(outputStream);
                System.out.println("MP3 file saved at: " + outputPath);
            }
        }
    }
}
