import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> map = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        for (File item : Objects.requireNonNull(pdfsDir.listFiles())) {
            PdfDocument doc = new PdfDocument(new PdfReader(item));
            int pages = doc.getNumberOfPages();
            for (int i = 1; i <= pages; ++i) {
                PdfPage pdfPage = doc.getPage(i);
                int page = doc.getPageNumber(pdfPage);
                String text = PdfTextExtractor.getTextFromPage(pdfPage);
                String[] words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }

                for (Map.Entry<String, Integer> rslts : freqs.entrySet()) {
                    List<PageEntry> pageEntry = new ArrayList<>();
                    if (!map.containsKey(rslts.getKey())) {
                        PageEntry entry = new PageEntry(item.getName(), page, rslts.getValue());
                        pageEntry.add(entry);
                        map.put(rslts.getKey(), pageEntry);
                    } else {
                        map.get(rslts.getKey()).add(new PageEntry(item.getName(), page, rslts.getValue()));
                    }
                }
                for (Map.Entry<String, List<PageEntry>> srch : map.entrySet()) {
                    List<PageEntry> compareList = srch.getValue();
                    Collections.sort(compareList);
                    map.put(srch.getKey(), compareList);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        if (map.containsKey(word)) {
            return map.get(word);
        }

        return new ArrayList<>();
    }
}
