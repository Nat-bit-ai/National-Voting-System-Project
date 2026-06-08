package util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import dao.ResultDAO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class PDFExporter {
    public void exportElectionResults(String electionTitle, List<ResultDAO.ResultRow> rows, Path output) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(output.toFile()));
        document.open();
        document.add(new Paragraph("National Digital Voting System"));
        document.add(new Paragraph("Election: " + electionTitle));
        document.add(new Paragraph(" "));
        for (ResultDAO.ResultRow row : rows) {
            document.add(new Paragraph(row.candidateName() + " - " + row.partyName() + ": " + row.voteCount()));
        }
        document.close();
    }
}
