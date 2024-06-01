package org.openjfx.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.ydev00.model.image.ImageData;
import com.ydev00.model.image.Image;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;

public class ImageHandler {

    public ImageHandler() {

    }

    public Image fileToImage(File file) throws IOException {
        BufferedImage bufferedImage =  ImageIO.read(file);
        String imageType = FilenameUtils.getExtension(String.valueOf(file));
        InputStream inputStream = new FileInputStream(file);

        List<Integer> binaryData = new ArrayList<>();
        for(int i = 0; i <  file.length(); i++)
            binaryData.add(inputStream.read());

        ImageData profileImgData = new ImageData(binaryData, false);
        return new Image(imageType ,profileImgData, bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    public javafx.scene.image.Image imageDataToImage(Image image){
        List<Integer> binaryData = image.getImage().getBinaryData();

        byte[] byteArray = new byte[binaryData.size()];
        for(int i = 0; i < binaryData.size(); i++){
            byteArray[i] = binaryData.get(i).byteValue();
        }
        return new javafx.scene.image.Image(new ByteArrayInputStream(byteArray));
    }
}
