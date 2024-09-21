package com.example.barcode.helper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BarcodeHelper {
    // Таблица кодирования символов для Code 39
    private static final Map<Character, String> CODE_39_MAP = new HashMap<>();

    static {
        CODE_39_MAP.put('0', "000110100");
        CODE_39_MAP.put('1', "100100001");
        CODE_39_MAP.put('2', "001100001");
        CODE_39_MAP.put('3', "101100000");
        CODE_39_MAP.put('4', "000110001");
        CODE_39_MAP.put('5', "100110000");
        CODE_39_MAP.put('6', "001110000");
        CODE_39_MAP.put('7', "000100101");
        CODE_39_MAP.put('8', "100100100");
        CODE_39_MAP.put('9', "001100100");

        CODE_39_MAP.put('A', "100001001");
        CODE_39_MAP.put('B', "001001001");
        CODE_39_MAP.put('C', "101001000");
        CODE_39_MAP.put('D', "000011001");
        CODE_39_MAP.put('E', "100011000");
        CODE_39_MAP.put('F', "001011000");
        CODE_39_MAP.put('G', "000001101");
        CODE_39_MAP.put('H', "100001100");
        CODE_39_MAP.put('I', "001001100");
        CODE_39_MAP.put('J', "000011100");

        CODE_39_MAP.put('K', "100000011");
        CODE_39_MAP.put('L', "001000011");
        CODE_39_MAP.put('M', "101000010");
        CODE_39_MAP.put('N', "000010011");
        CODE_39_MAP.put('O', "100010010");
        CODE_39_MAP.put('P', "001010010");
        CODE_39_MAP.put('Q', "000000111");
        CODE_39_MAP.put('R', "100000110");
        CODE_39_MAP.put('S', "001000110");
        CODE_39_MAP.put('T', "000010110");

        CODE_39_MAP.put('U', "110000001");
        CODE_39_MAP.put('V', "011000001");
        CODE_39_MAP.put('W', "111000000");
        CODE_39_MAP.put('X', "010010001");
        CODE_39_MAP.put('Y', "110010000");
        CODE_39_MAP.put('Z', "011010000");

        CODE_39_MAP.put('-', "010000101");
        CODE_39_MAP.put('.', "110000100");
        CODE_39_MAP.put(' ', "011000100");
        CODE_39_MAP.put('$', "010101000");
        CODE_39_MAP.put('/', "010100010");
        CODE_39_MAP.put('+', "010001010");
        CODE_39_MAP.put('%', "000101010");

        CODE_39_MAP.put('*', "010010100"); // Start/Stop символ
    }

    // Метод для генерации штрих-кода
    public static StringBuilder generateBarcode(String text) {
        StringBuilder barcodePattern = new StringBuilder();
        barcodePattern.append(CODE_39_MAP.get('*'));

        for (char c : text.toUpperCase().toCharArray()) {
            if (CODE_39_MAP.containsKey(c)) {
                barcodePattern.append(CODE_39_MAP.get(c));
            } else {
                throw new IllegalArgumentException("CODE_39_MAP doesn't contains such character: " + c);
            }
        }

        // Добавляем стоп-символ '*'
        barcodePattern.append(CODE_39_MAP.get('*'));
        return barcodePattern;
    }

    public static void saveBarcode(String fullPathAndName, StringBuilder barcodePattern) throws IOException {
        // Размер изображения
        int width = barcodePattern.length() * 2;
        int height = 100;

        // Создаем изображение
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Заливка фона белым цветом
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Рисуем штрих-код
        int x = 0;
        for (char bit : barcodePattern.toString().toCharArray()) {
            Color color = bit == '1' ? Color.BLACK : Color.WHITE;
            g2d.setPaint(color);
            g2d.fillRect(x, 0, 2, height);
            x += 2;
        }

        g2d.dispose();
        fullPathAndName += ".png";
        ImageIO.write(image, "png", new File(fullPathAndName));
    }

    public static String decodeBarcode(String path) {
        try {
            var image = ImageIO.read(new File(path));
            // Получаем ширину изображения (количество битов штрих-кода)
            int width = image.getWidth();
            int height = image.getHeight();

            StringBuilder barcodeBits = new StringBuilder();

            // Считываем изображение, получая биты (черный = 1, белый = 0)
            for (int x = 0; x < width; x += 2) { // Шаг 2, так как каждая линия — 2 пикселя
                int pixelColor = image.getRGB(x, 0); // Берем пиксель сверху изображения
                Color color = new Color(pixelColor);

                // Если пиксель черный, добавляем 1, если белый - 0
                if (color.equals(Color.BLACK)) {
                    barcodeBits.append('1');
                } else {
                    barcodeBits.append('0');
                }
            }

            // Теперь у нас есть строка из битов штрих-кода
            String bits = barcodeBits.toString();

            // Декодируем биты в символы, используя карту CODE_39_MAP
            StringBuilder decodedText = new StringBuilder();
            for (int i = 0; i <= bits.length() - 9; i += 9) {
                String bitSegment = bits.substring(i, i + 9);

                // Находим соответствующий символ в CODE_39_MAP
                for (Map.Entry<Character, String> entry : CODE_39_MAP.entrySet()) {
                    if (entry.getValue().equals(bitSegment)) {
                        char decodedChar = entry.getKey();

                        // Пропускаем старт/стоп символ '*' при декодировании
                        if (decodedChar != '*') {
                            decodedText.append(decodedChar);
                        }
                        break;
                    }
                }
            }

            return decodedText.toString();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
}
