package org.openjfx.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.ydev00.model.image.ImageData;
import com.ydev00.model.image.Image;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
/**
 * A classe ImageHandler fornece métodos utilitários para conversão entre arquivos de imagem e objetos de imagem personalizados.
 */
public class ImageHandler {

    /**
     * Construtor padrão da classe ImageHandler.
     */
    public ImageHandler() {

    }

    /**
     * Converte um arquivo de imagem em um objeto de imagem personalizado.
     *
     * @param file O arquivo de imagem a ser convertido.
     * @return Um objeto {@link Image} representando a imagem.
     * @throws IOException Se ocorrer um erro de leitura do arquivo.
     */
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

    /**
     * Converte um objeto de imagem personalizado em um objeto de imagem do JavaFX.
     *
     * @param image O objeto de imagem personalizado.
     * @return Um objeto {@link javafx.scene.image.Image} representando a imagem.
     */
    public javafx.scene.image.Image imageDataToImage(Image image){
        List<Integer> binaryData = image.getImage().getBinaryData();

        byte[] byteArray = new byte[binaryData.size()];
        for(int i = 0; i < binaryData.size(); i++){
            byteArray[i] = binaryData.get(i).byteValue();
        }
        return new javafx.scene.image.Image(new ByteArrayInputStream(byteArray));
    }
}
