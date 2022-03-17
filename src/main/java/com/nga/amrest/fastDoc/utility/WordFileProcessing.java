package com.nga.amrest.fastDoc.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

public class WordFileProcessing {

	public static XWPFDocument createWordFile(MultipartFile multipartFile) throws IOException {
		// Function required to create word .docx file from Bytes array
		/*
		 * File tempFile = File.createTempFile("template", ".docx");
		 * tempFile.deleteOnExit(); FileOutputStream fos = new
		 * FileOutputStream(tempFile); fos.write(bytesArr);
		 */
		XWPFDocument document = new XWPFDocument(multipartFile.getInputStream());
//		System.out.println("Temp file : " + tempFile.getAbsolutePath());
//		fos.close();
		return document;
	}

	public static JSONArray getTags(XWPFDocument docxDocument) throws FileNotFoundException, IOException {
		// For extracting all the tags in the word file
		@SuppressWarnings("resource")
		XWPFWordExtractor we = new XWPFWordExtractor(docxDocument);
		Matcher m = Pattern.compile("\\.(.*?)\\]").matcher(we.getText());
		JSONArray tags = new JSONArray();
		while (m.find()) {
			tags.put(m.group(1));
		}
		return tags;
	}
}