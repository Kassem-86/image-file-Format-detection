
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Dell
 */
public class ImageFormatDetector {
    
/*" my samples
    D:\project multimedia image\file_example_PNG_500kB.png"
    "D:\project multimedia image\sample_640×426.jpeg"
    "D:\project multimedia image\SampleGIFImage_40kbmb.gif"
    "D:\project multimedia image\file_example_TIFF_1MB.tiff"
    "D:\project multimedia image\sample_640×426.bmp"*/
    public static void main(String[] args) throws IOException {
        Scanner s=new Scanner(System.in);
        System.out.println("File Type:");
        try ( FileInputStream inputStream = new FileInputStream("file"))// replace with the path to your imafe file
 {
            byte[] buffer = new byte[8];
            int ReadBytes = inputStream.read(buffer);
            if (buffer[0] == (byte) 0xFF && buffer[1] == (byte) 0xD8) {
                System.out.println("JPEG");
            } else if (buffer[0] == (byte) 0x89 && buffer[1] == (byte) 0x50 && buffer[2] == (byte) 0x4E && buffer[3] == (byte) 0x47 && buffer[4] == (byte) 0xD && buffer[5] == (byte) 0xA && buffer[6] == (byte) 0x1A && buffer[7] == (byte) 0xA) {
                System.out.println("PNG");
            } else if (buffer[0] == (char) 'G' && buffer[1] == (char) 'I' && buffer[2] == (char) 'F') {
                System.out.println("GIF");

            } else if (buffer[0] == (char) 'B' && buffer[1] == 'M') {
                System.out.println("BMP");
            } else if ((buffer[0] == (byte) 0x49 && buffer[1] == (byte) 0x49 && buffer[2] == (byte) 0x2A && buffer[3] == (byte) 0x00) || (buffer[0] == (byte) 0x4D && buffer[1] == (byte) 0x4D && buffer[2] == (byte) 0x00 && buffer[3] == (byte) 0x2A)) {//for little and big endian conditions
                System.out.println("TIFF ");
            }
            else if(buffer[0]==(byte)0x46 && buffer[1]==(byte)0x55&& buffer[2]==(byte)0x4A && buffer[3]==(byte)0x49){//raw (FUJI) signature
                System.out.println("RAW");
            }
            
            else{System.out.println("unspported File Format");}
        }

    }
}
/*
JPEG 
Signature: 0xFFD8FF
The first two bytes are always 0xFFD8, and the last two bytes are always 0xFFD9. (The first byte is "0xFF.")
(The second byte is "0xD8.")
                                                    
PNG 
Signature: 0x89504E470D0A1A0A
The first eight bytes are always 0x89504E470D0A1A0A.

GIF 
Signature: "GIF" (ASCII)
The first three bytes are always "GIF" (ASCII).

BMP
Signature: "BM" (ASCII)
The first two bytes are always "BM" (ASCII).

TIFF (Tagged Image File Format)
Signature: 0x49492A00 (little-endian) or 0x4D4D002A (big-endian)
The first four bytes can be "II*\x00" (little-endian) or "MM\x00*" (big-endian).

RAW(FUJI) :
Signature: 0x46554A49

*/
