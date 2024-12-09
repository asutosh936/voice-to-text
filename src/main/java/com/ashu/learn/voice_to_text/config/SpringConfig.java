package com.ashu.learn.voice_to_text.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.cmu.sphinx.api.LiveSpeechRecognizer;


@Configuration
public class SpringConfig {
    

    @Bean
    public String textToSpeechConfig(){
        try{
            edu.cmu.sphinx.api.Configuration configuration = new edu.cmu.sphinx.api.Configuration();
            // Set the path to the acoustic model
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            
            // Set the path to the dictionary
            configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
            
            // Set the path to the language model (optional, or use a custom grammar)
            configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

            // Create a live recognizer
            LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);

            System.out.println("Say something...");

            // Start recognition process (microphone)
            recognizer.startRecognition(true);
            String speechResult;

            // Process speech until the user stops talking
            while ((speechResult = recognizer.getResult().getHypothesis()) != null) {
                System.out.println("You said: " + speechResult);

                // Stop the program if the user says "exit"
                if (speechResult.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting...");
                    break;
                }
            }

            // Stop recognition
            recognizer.stopRecognition();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "Success";
    }

    // @Bean
    // public String transcribe(){

    //     try {
    //         // Initialize AWS Transcribe Streaming Client
    //         TranscribeStreamingAsyncClient transcribeClient = TranscribeStreamingAsyncClient.builder()
    //             .region(Region.US_WEST_2)  // Choose appropriate region
    //             .credentialsProvider(ProfileCredentialsProvider.create())
    //             .build();

    //         // Start real-time transcription
    //         startRealTimeTranscription(transcribeClient);

    //     } catch (Exception e) {
    //         System.err.println("Transcription error: " + e.getMessage());
    //         e.printStackTrace();
    //     }
    //     return "Transribe-Success";
    // }

    // private static void startRealTimeTranscription(TranscribeStreamingAsyncClient transcribeClient) 
    //     throws LineUnavailableException {
        
    //     // Audio format configuration
    //     AudioFormat audioFormat = new AudioFormat(
    //         SAMPLE_RATE,  // sample rate
    //         16,           // sample size in bits
    //         1,            // mono
    //         true,         // signed
    //         false         // little endian
    //     );

    //     // Microphone input stream
    //     DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
    //     TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
    //     microphone.open(audioFormat);
    //     microphone.start();

    //     // Transcription request configuration
    //     StartStreamTranscriptionRequest request = StartStreamTranscriptionRequest.builder()
    //         .languageCode(LanguageCode.EN_US)
    //         .mediaEncoding(MediaEncoding.PCM)
    //         .mediaSampleRateHertz(SAMPLE_RATE)
    //         .build();

    //     // Create async transcription handler
    //     CompletableFuture<Void> transcriptionFuture = transcribeClient.startStreamTranscription(
    //         request, 
    //         new AudioStreamPublisher(microphone),
    //         new TranscriptionResponseHandler()
    //     );

    //     // Keep transcription running for 60 seconds
    //     try {
    //         Thread.sleep(60000);
    //     } catch (InterruptedException e) {
    //         Thread.currentThread().interrupt();
    //     } finally {
    //         microphone.stop();
    //         microphone.close();
    //     }
    // }

    // // Custom audio stream publisher for AWS Transcribe
    // private static class AudioStreamPublisher implements Publisher<AudioStream> {
    //     private final TargetDataLine microphone;

    //     public AudioStreamPublisher(TargetDataLine microphone) {
    //         this.microphone = microphone;
    //     }

    //     @Override
    //     public void subscribe(Subscriber<? super AudioStream> subscriber) {
    //         subscriber.onSubscribe(new Subscription() {
    //             @Override
    //             public void request(long n) {
    //                 try {
    //                     byte[] buffer = new byte[BUFFER_SIZE];
    //                     int bytesRead = microphone.read(buffer, 0, buffer.length);
                        
    //                     if (bytesRead > 0) {
    //                         ByteBuffer audioBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);
    //                         AudioStream audioStream = AudioStream.builder()
    //                             .audioChunk(SdkBytes.fromByteBuffer(audioBuffer))
    //                             .build();
                            
    //                         subscriber.onNext(audioStream);
    //                     }
                        
    //                     if (bytesRead < buffer.length) {
    //                         subscriber.onComplete();
    //                     }
    //                 } catch (Exception e) {
    //                     subscriber.onError(e);
    //                 }
    //             }

    //             @Override
    //             public void cancel() {
    //                 microphone.stop();
    //                 microphone.close();
    //             }
    //         });
    //     }
    // }

    // // Custom transcription response handler
    // private static class TranscriptionResponseHandler 
    //     implements StreamingResponseHandler<StartStreamTranscriptionResponse, Void> {
        
    //     private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    //     @Override
    //     public CompletableFuture<Void> handle(SdkPublisher<StartStreamTranscriptionResponse> publisher) {
    //         return CompletableFuture.runAsync(() -> {
    //             publisher.subscribe(new Subscriber<StartStreamTranscriptionResponse>() {
    //                 @Override
    //                 public void onSubscribe(Subscription s) {
    //                     s.request(Long.MAX_VALUE);
    //                 }

    //                 @Override
    //                 public void onNext(StartStreamTranscriptionResponse response) {
    //                     // Process transcription results
    //                     if (response.transcript() != null) {
    //                         response.transcript().results().forEach(result -> {
    //                             if (result.isPartial()) {
    //                                 System.out.println("Partial Transcript: " + 
    //                                     result.alternatives().get(0).transcript());
    //                             } else {
    //                                 System.out.println("Final Transcript: " + 
    //                                     result.alternatives().get(0).transcript());
    //                             }
    //                         });
    //                     }
    //                 }

    //                 @Override
    //                 public void onError(Throwable t) {
    //                     System.err.println("Transcription error: " + t.getMessage());
    //                 }

    //                 @Override
    //                 public void onComplete() {
    //                     System.out.println("Transcription completed.");
    //                 }
    //             });
    //         }, executorService);
    //     }
    // }
}
