import org.apache.batik.apps.rasterizer.DestinationType;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.batik.apps.rasterizer.SVGConverterException;
import org.apache.batik.apps.rasterizer.SVGConverterURLSource;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PresentationDownloader {

    private static final String SOURCE_PATH = "src/main/resources/lecture";

    private static final String OUTPUT_PATH = "output";

    private static final String URL_PATH_TEST = "https://www.flaticon.com/svg/vstatic/svg/1089/1089270.svg?token=exp=1614629125~hmac=ff8e9b8ee3edeb9baa42d1c6762a6caf";

    private static final String URL_PATH = "https://bbb5.ssau.ru/bigbluebutton/presentation/097c80a16ee9277077ca6a347f6e8f9c597b9a62-1616484763156/097c80a16ee9277077ca6a347f6e8f9c597b9a62-1616484763156/e5bb9b1057dc497b46d44143817b9b257fe0ebb3-1616486304542/svg";

    public static void main(String[] args) {

        fullCycleTest(URL_PATH, 22);

    }

    public static void fullCycleTest(String url, int count) {
        for (int i = 1; i <= count; i++) {
            GetOneSvgFromUrl(String.format("%s/%d", url, i));
        }
    }

    /* Первый вариант как скачать один свг-файл и сохранить в пдф */
    public static void DownloadOneSvgFromUrl(String url) {
        try {
            SVGConverterURLSource converter = new SVGConverterURLSource(url);
            PDFTranscoder transcoder = new PDFTranscoder();
            TranscoderInput transcoderInput = new TranscoderInput(converter.openStream());
            TranscoderOutput transcoderOutput = new TranscoderOutput(new FileOutputStream("output/urlTest1"));
            transcoder.transcode(transcoderInput, transcoderOutput);
        } catch (SVGConverterException | TranscoderException | IOException e) {
            e.printStackTrace();
        }
    }

    /* Второй вариант как скачать один свг-файл и сохранить в пдф */
    public static void GetOneSvgFromUrl(String url) {
        SVGConverter svgConverter = new SVGConverter();
        String[] sources = new String[]{url};
        svgConverter.setSources(sources);

        svgConverter.setDestinationType(DestinationType.PDF);
        File output = new File("output/urlTest5");
        svgConverter.setDst(output);
        try {
            svgConverter.execute();
        } catch (SVGConverterException e) {
            e.printStackTrace();
        }
    }

    /* Тест на объединение нескольких пдф-файлов */
    public static void mergePDFs() {
        PDFMergerUtility mergerUtility = new PDFMergerUtility();
        try {
            mergerUtility.addSource("output/1.pdf");
            mergerUtility.addSource("output/2.pdf");
            mergerUtility.addSource("output/3.pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mergerUtility.setDestinationFileName("output/merged.pdf");
        try {
            mergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Тест на конвертацию нескольких свг-файлов в отдельные пдф-файлы */
    public static void convertSVGtoPDF() {
        SVGConverter svgConverter = new SVGConverter();
        String[] sources = {"src/main/resources/lecture/1.svg", "src/main/resources/lecture/2.svg", "src/main/resources/lecture/3.svg"};
        svgConverter.setSources(sources);

        svgConverter.setDestinationType(DestinationType.PDF);
        File output = new File(OUTPUT_PATH);
        svgConverter.setDst(output);
        try {
            svgConverter.execute();
        } catch (SVGConverterException e) {
            e.printStackTrace();
        }

//        PDFTranscoder transcoder = new PDFTranscoder();
//        try (FileInputStream fileInputStream = new FileInputStream(svgFile); FileOutputStream fileOutputStream = new FileOutputStream(new File("./target/test-batik.pdf"))) {
//            TranscoderInput transcoderInput = new TranscoderInput(fileInputStream);
//            TranscoderOutput transcoderOutput = new TranscoderOutput(fileOutputStream);
//            transcoder.transcode(transcoderInput, transcoderOutput);
//        }
    }
}
