
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioFormatDetector {
    Scanner s=new Scanner(System.in);

    public static void main(String[] args) throws UnsupportedAudioFileException {
        String filePath = ("D:/project multimedia . wav file/file_example_WAV_1MG.wav"); // Replace with the path to your audio file
        //printFirstBytes(filePath, 6);
        try {
            File file = new File(filePath);

            // Check if the file is a valid WAV or mp3
            if (isWAVFile(file)) {
                System.out.println("Reading.WAV file......");
                readWavHeader(file);
            } else if (isMp3File(file)) {
                displayMP3Header(filePath);

            } else {
                System.out.println("Not a valid WAV or mp3 file.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isWAVFile(File file) throws IOException, UnsupportedAudioFileException {
        try ( FileInputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[44]; //  required bytes to check the WAV signature

            int bytesRead = inputStream.read(buffer, 0, 44);
            return bytesRead == 44 && WAV_sign(buffer, file);
        }
    }

    private static void readWavHeader(File file) throws IOException {
        try ( AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file)) {
            try ( FileInputStream inputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[44];
                int bytesRead = inputStream.read(buffer, 0, 44);
                AudioFormat format = audioInputStream.getFormat();
                System.out.println("Header Information:");
                String chunkID = new String(buffer, 0, 4);
                System.out.println("chunkID: " + chunkID);
                String chunkSize = new String(buffer, 4, 4);
                System.out.println("chunkSize: " + ConvertbyteInt(buffer, 4));
                String Format = new String(buffer, 8, 4);
                System.out.println("Format: " + Format);
                String subchunk1ID = new String(buffer, 12, 4);
                System.out.println("Subchunk1ID: " + subchunk1ID);
                String subchunk1Size = new String(buffer, 16, 4);
                System.out.println("Subchunk1Size: " + ConvertbyteInt(buffer, 16));
                String AudioFormat = new String(buffer, 20, 2);
                System.out.println("AudioFormat: " + ConvertbyteShort(buffer, 20));
              String NumChannels = new String(buffer, 22, 2);
                System.out.println("NumOfChannels: " +ConvertbyteShort(buffer, 22) );
               String SampleRate = new String(buffer, 24, 4);
                System.out.println("SampleRate: " + ConvertbyteInt(buffer, 24));
                String ByteRate = new String(buffer, 28, 4);
                System.out.println("ByteRate: " + ConvertbyteInt(buffer, 28));
                String BlockAllign = new String(buffer, 32, 2);
                System.out.println("BlockAllign: " + ConvertbyteShort(buffer, 32));
                String subchunk2ID = new String(buffer, 36, 4);
                System.out.println("Subchunk2ID: " + subchunk2ID);
                String subchunk2Size = new String(buffer, 40, 4);
                System.out.println("Subchunk2Size: " + ConvertbyteInt(buffer, 40));

            }
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(AudioFormatDetector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static boolean isMp3File(File file) throws IOException {
        try ( FileInputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[11]; // required bytes to check the Mp3 signature

            // Read the first 4 bytes into the buffer
            int bytesRead = inputStream.read(buffer, 0, 11);
            String Read_ID3 = new String(buffer, 0, 3);
            return bytesRead == 11 && (buffer[0] == (char) 'I' && buffer[1] == 'D' && buffer[2] == '3');
        }

    }

    private static boolean WAV_sign(byte[] buffer, File file) throws FileNotFoundException, IOException {
        boolean retval;
        try ( FileInputStream inputStream = new FileInputStream(file)) {
            int bytesRead = inputStream.read(buffer, 0, 44);
            String chunkID = new String(buffer, 0, 4);
            String Format = new String(buffer, 8, 4);
            if ((buffer[0] == (char) 'R' && buffer[1] == 'I' && buffer[2] == 'F' && buffer[3] == 'F')) {
                retval = true;
            } else {
                retval = false;
            }
        }
        return retval;
    }

    private static void printFirstBytes(String filePath, int numBytes) {
        try ( FileInputStream inputStream = new FileInputStream(filePath)) {
            byte[] bytes = new byte[numBytes];
            int bytesRead = inputStream.read(bytes);

            if (bytesRead > 0) {
                System.out.printf("First %d bytes of the file in hexadecimal:%n", numBytes);

                for (byte b : bytes) {
                    System.out.printf("%02X ", b);
                }
            } else {
                System.out.println("Error reading the file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayMP3Header(String filePath) throws IOException {
        try ( FileInputStream inputStream = new FileInputStream(filePath)) {
            byte[] headerBytes = new byte[12];
            int bytesRead = inputStream.read(headerBytes);

            if (bytesRead == 12) {
                System.out.println("Displaying MP3 header:");

                // Display interpreted information
                System.out.println("MPEG Version: " + getMpegVersion(headerBytes[1]));
                System.out.println("Layer: " + getLayer(headerBytes[1]));
                System.out.println("Bitrate: " + getBitrate(headerBytes[2]));
                System.out.println("Private Bit:" + getPrivateBit(headerBytes[2]));
                System.out.println("Sample Rate: " + getSampleRate(headerBytes[3]));
                System.out.println("padding: " + getpadding(headerBytes[4]));
                System.out.println("Channel Mode: " + getChannelMode(headerBytes[6]));
                System.out.println("extention :" + getChannelModeExtension(headerBytes[6]));
                System.out.println("CopyRight Bit:" + getCopyrightBit(headerBytes[6]));
                System.out.println("Original Bit:" + getOriginalBit(headerBytes[6]));
                System.out.println("Emphasis:" + getEmphasis(headerBytes[6]));

            } else {
                System.out.println("Error reading MP3 header.");
            }
        }

    }

    


    private static String getMpegVersion(byte byte1) {  //(assume) byte 1 in hexa(44)->(00001000(in binary))&00000011
        int version = (byte1 >> 3) & 0x03; // we used 0x03 to get the least 2 bits
        return "MPEG-" + version;
    }

    private static int getLayer(byte byte1) { //00100010&00000011=2(layer=2)

        int layer = (byte1 >> 1) & 0x03;
        return layer;
    }

    private static int getPrivateBit(byte byte2) {
        return (byte2 >> 7) & 0x01;
    }

    private static String getBitrate(byte byte2) { //assume byte 2 in hexa =(33) ->in binary= 00110011    00110000&00001111=00000011=3(in hexa)(in case 3 at getbitrate string bit rate = 48kbps)
        int bitrateIndex = (byte2 >> 4) & 0x0F;// we used 0x0f to get the least 4 bits
        return getBitrateString(bitrateIndex);
    }

    private static String getSampleRate(byte byte3) { // assume byte 3=(3)in hexa-> 00000011(in binary) 00000000&00000011=00000000 = 0(case 0 = 44100)
        int sampleRateIndex = (byte3 >> 2) & 0x03;
        return getSampleRateString(sampleRateIndex);
    }

    private static int getpadding(byte byte4) {
        int padding = (byte4 >> 4) & 0x03;  //byte 4=00  00000000&00000011=00000000=0
        return padding;
    }

    private static int getCopyrightBit(byte byte6) {
        // Bit 3 represents the copyright bit
        int CopyRightBIt = (byte6 >> 3) & 0x01;
        return CopyRightBIt;
    }

    private static int getChannelModeExtension(byte byte6) {
        // Bits 4 and 5 represent the channel mode extension
        return (byte6 >> 4) & 0x03;
    }

    private static int getOriginalBit(byte byte6) {
        // Bit 2 represents the original bit

        int Original_bit = (byte6 >> 2) & 0x01;
        return Original_bit;
    }

    private static int getEmphasis(byte byte6) {
        // Bits 0 and 1 represent the emphasis
        return byte6 & 0x03;
    }

    private static String getChannelMode(byte byte6) { //00000000&00000011=00000000(stereo (case 0 in get channel mode string))
        int mode = (byte6 >> 6) & 0x03;
        return getChannelModeString(mode);
    }

    private static String getBitrateString(int index) {
        switch (index) {
            case 0:
                return "Free";
            case 1:
                return "32 kbps";
            case 2:
                return "40 kbps";
            case 3:
                return "48 kbps";
            case 4:
                return "56 kbps";
            case 5:
                return "64 kbps";
            case 6:
                return "80 kbps";
            case 7:
                return "96 kbps";
            case 8:
                return "112 kbps";
            case 9:
                return "128 kbps";
            case 10:
                return "160 kbps";
            case 11:
                return "192 kbps";
            case 12:
                return "224 kbps";
            case 13:
                return "256 kbps";
            case 14:
                return "320 kbps";
            default:
                return "Unknown";
        }
    }

    private static String getSampleRateString(int index) {
        switch (index) {
            case 0:
                return "44100Hz";
            case 1:
                return "48000Hz";
            case 2:
                return "32000hz";

            default:
                return "Unknown";
        }
    }

    private static String getChannelModeString(int mode) {
        switch (mode) {
            case 0:
                return "Stereo";
            case 1:
                return "Joint Stereo";
            case 2:
                return "Dual Channel";
            case 3:
                return "Single Channel";
            default:
                return "Unknown";
        }
    }

    private static short ConvertbyteShort(byte[] bytes, int offset) {

        return (short) ((bytes[offset + 1] & 0xFF) << 8 | (bytes[offset] & 0xFF));

    }

    private static int ConvertbyteInt(byte[] bytes, int offset) {

        return (bytes[offset + 3] & 0xFF) << 24 | (bytes[offset + 2] & 0xFF) << 16 | (bytes[offset + 1] & 0xFF) << 8 | (bytes[offset] & 0xFF);

    }

}
