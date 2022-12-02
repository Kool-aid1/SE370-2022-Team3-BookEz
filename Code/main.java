
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument; 
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.text.PDFTextStripper;
//import org.testing.annotations.Test;


public class main {

    public static void main(String args[]) throws IOException  {

        File file = new File("/Users/kennyly/Desktop/Sales_record_eBay_Seller_Hub (1).pdf");
        FileInputStream fis = new FileInputStream(file);

        PDDocument pdfDocument = PDDocument.load(fis);
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String docText = pdfTextStripper.getText(pdfDocument);

        if(docText.contains("total")){
            System.out.println("FOUND");
        }

        extractInfo(docText);

        createPDF();

        fis.close();
        pdfDocument.close();

    }

    
    public static void extractInfo(String s){

        String orderNum, total, shipCost, soldPrice;
        double fullTotal, fullshipCost, fullPrice;

        //gets order number
        int orderNumStart = s.indexOf("Order");
        int orderNumEnd = s.indexOf("\n");
        orderNum = s.substring(orderNumStart, orderNumEnd);

        System.out.println(orderNum);

        //gets order total
        int totalStart = s.indexOf("$", orderNumEnd++);//looks for order after end of order number
        int totalEnd = s.indexOf("\n", totalStart);
        total = s.substring(totalStart+1, totalEnd);

        System.out.println(total);

        int shipCostStart = s.indexOf("$", totalEnd);
        int shipCostEnd = s.indexOf("\n", shipCostStart);
        shipCost = s.substring(shipCostStart+1, shipCostEnd);

        System.out.println(shipCost);

        int soldPriceStart = s.indexOf("$", shipCostEnd);
        int soldPriceEnd = s.indexOf("\n", soldPriceStart);
        soldPrice = s.substring(soldPriceStart+1, soldPriceEnd);

        System.out.println(soldPrice);

        fullshipCost = Double.parseDouble(shipCost);
        fullPrice = Double.parseDouble(soldPrice);

        fullTotal = fullshipCost+ fullPrice;

        System.out.printf("$%.2f",fullTotal);

    }
    
    public static void createPDF() throws IOException{

        PDDocument document = new PDDocument();
        PDPage firstPage = new PDPage();
        document.addPage(firstPage);

        int pageHeight = (int) firstPage.getTrimBox().getHeight();
        int pageWidth = (int) firstPage.getTrimBox().getWidth();

        PDPageContentStream contentStream = new PDPageContentStream(document, firstPage);

        contentStream.setStrokingColor(Color.BLACK);
        contentStream.setLineWidth(1);

        int initX = 50;
        int initY = pageHeight-50;
        int cellHeight = 30;
        int cellWidth = 100;
        int colCount = 5;
        int rowCount = 10;

        for(int i = 0; i < rowCount; i++){
            for(int j = 0; j < colCount; i++){
                contentStream.addRect(initX, initY, cellWidth, -cellHeight);

                initX = cellWidth;
            }
            initX = 50;
            initY -= cellHeight;
        }

        contentStream.stroke();

        document.save("PDF.pdf");
        System.out.println("PDF created");

        contentStream.close();
        document.close();
    }
}