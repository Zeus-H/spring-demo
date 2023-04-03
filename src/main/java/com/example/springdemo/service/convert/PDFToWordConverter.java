package com.example.springdemo.service.convert;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFToWordConverter {
    public static void main(String[] args) throws IOException {
        // PDF文件路径
        String pdfPath = "C:\\Users\\2588\\Desktop\\22.pdf";
        
        // 读取PDF文本
        PDDocument pdfDoc = PDDocument.load(new File(pdfPath));
        PDFTextStripper stripper = new PDFTextStripper();
        String pdfText = stripper.getText(pdfDoc);

        // 创建Word文档
        XWPFDocument docx = new XWPFDocument();

        // 写入Word文本
        docx.createParagraph().createRun().setText(pdfText);

        // 将Word文档写入文件
        FileOutputStream fos = new FileOutputStream("C:\\Users\\2588\\Desktop\\22.doc");
        docx.write(fos);
        fos.close();
        
        // 关闭PDF文档
        pdfDoc.close();
    }
}
