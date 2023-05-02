public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    int getCount() {
        return count;
    }

    @Override
    public int compareTo(PageEntry o) {
      if(this.count == o.getCount()){
          return 0;
      }else if(this.count < o.getCount()){
          return -1;
      }else {
          return 1;
      }
    }

    @Override
    public String toString() {
        return "Документ: " + pdfName + ", страница: " + page + ", кол-во: " + count;
    }
}
