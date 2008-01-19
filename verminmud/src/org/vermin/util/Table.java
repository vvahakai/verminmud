/* TableBuilder.java
	17.5.2002	VV

ChangeLog:

18.5.2002 TTa  Major rewrite
*/

package org.vermin.util;

import java.util.Vector;
import java.util.StringTokenizer;

/**
 * Creates ascii tables.
 * Example:
 * <code>
 * Table t = new Table();
 * t.addHeader("Erno", 20, ALIGN_MIDDLE);
 * t.addHeader("%", 3, ALIGN_MIDDLE);
 *
 * t.addRow();
 * t.addColumn("tämä teksti on liian pitkä ja se rivitetään sopimaan"+
 *     " koko systeemeihin ja muuta mahtavaa boonusta", 10, ALIGN_MIDDLE, true);
 * t.addColumn("12", 3, ALIGN_RIGHT);
 * t.addColumn("crappi", 7, ALIGN_MIDDLE);
 *
 * t.addRow();
 * t.addColumn("Tekstiä myös", 20, ALIGN_MIDDLE, true);
 * t.addColumn("85", 3, ALIGN_LEFT);
 * 
 * String result = t.render();
 * </code>
 * @author Tatu Tarvainen
 */
public class Table {

   public static final int ALIGN_LEFT = 0;
   public static final int ALIGN_MIDDLE = 1;
   public static final int ALIGN_RIGHT = 2;
   
   public static class Cell {
      public String text;
      public int align;
      public boolean wrap;
      public int length;

      public Cell(String txt, int len, int ali, boolean wrp) {
         text = txt;
         length = len;
         align = ali;
         wrap = wrp;
      }
   }

   private Vector header;
   private Vector rows;
   private Vector currentRow;

   public Table() {
      header = new Vector();
      rows = new Vector();
      currentRow = null;
   }
   
   /**
    * Add a new cell to the header.
    * 
    * @param data  data to write in the header, data.toString() will be used to display data
    * @param len  the lenght in characters to reserve for this column
    * @param align  one of the constants ALIGN_LEFT, ALIGN_MIDDLE or ALIGN_RIGHT
    */
   public void addHeader(Object data, int len, int align) {
      header.add(new Cell(data.toString(), len, align, false));
   }

   /**
    * Initalizes a new row in the table. Must be called before
    * individual cells are added via addColumn.
    */
   public void addRow() {
      currentRow = new Vector();
      rows.add(currentRow);
   }

   /**
    * Adds a new cell to the table. Cells are added in a left-to-right
    * top-to-down order. Make sure you initalize a new row when needed
    * by calling addRow.
    * 
    * @param data  data to write in the header, data.toString() will be used to display data
    * @param len  the lenght in characters to reserve for this cell
    * @param align  one of the constants ALIGN_LEFT, ALIGN_MIDDLE or ALIGN_RIGHT
    * @param wrap  pass true if you want automatic wrapping for texts longer than len
    */
   public void addColumn(Object data, int len, int align, boolean wrap) {
      if(currentRow == null) {
         throw new RuntimeException("No row. First use addRow()!");
      }

      currentRow.add(new Cell(data.toString(), len, align, wrap));
   }

   public void addColumn(Object data, int len, int align) {
      addColumn(data, len, align, false);
   }

   public void addColumn(Object data, int len) {
      addColumn(data, len, ALIGN_LEFT, false);
   }

   public void addColumn(Object data) {
	   addColumn(data, data.toString().length());
   }
   
   private int[] calculateColumnWidths() {

      // calculate column count
      int columns = header.size();
      for(int i=0; i<rows.size(); i++) {
         int rowCols = ((Vector) rows.get(i)).size();
         columns = columns < rowCols ? rowCols : columns;
      }

      // calculate width for each column
      int width[] = new int[columns];
      for(int i=0; i<columns; i++) {
         if(header.size() > i) {
            Cell c = (Cell) header.get(i);
            width[i] = c.length;
            continue; // TT 23.8.2005 (don't allow changing a specified size)
         } else {
            width[i] = 0;
         }

         for(int j=0; j<rows.size(); j++) {
            Vector row = (Vector) rows.get(j);
            if(row.size() > i) {
               Cell c = (Cell) row.get(i);
               width[i] = width[i] < c.length ? c.length : width[i];
            }
         }
      }

      return width;
   }
      
	public String render() {
		StringBuffer table = new StringBuffer(".");

      int[] width = calculateColumnWidths();

		// composing the first horizontal line
		for(int i=0; i < header.size(); i++) {
         table.append(makePadding('-', width[i]+2)+".");
		}
		table.append("\n| ");

		// composing headers from first row of data
		for(int i=0; i < header.size(); i++) {
         Cell c = (Cell) header.get(i);
         table.append(padText(c.text, width[i], c.align)+" | ");
		}
		table.append("\n+");

		// composing horizontal line after the headers
		for(int i=0; i < width.length; i++) {
         table.append(makePadding('-', width[i]+2)+"+");
		}
		table.append("\n");

		// composing multiple rows of data
		for(int i=0; i < rows.size(); i++) {
         table.append(renderRow((Vector) rows.get(i), width));
      }

		// composing last horizontal line
		table.append("`");
		for(int i=0; i < width.length; i++) {
         table.append(makePadding('-', width[i]+2)+"`");
		}
		table.append("\n");

		return table.toString();
	}

   private String renderRow(Vector row, int[] width) {
      StringBuffer sb = new StringBuffer();
      String[][] data = new String[width.length][];

      int lines = 0;
      for(int i=0; i<width.length; i++) {
         if(row.size() > i) {
            Cell c = (Cell) row.get(i);
            if(c.wrap) {
               data[i] = wordWrap(c.text, width[i], c.align);
            } else {
               data[i] = new String[] { padText(c.text, width[i], c.align) };
            }
         } else {
            data[i] = new String[] { makePadding(' ', width[i]) };
         }
         lines = (data[i].length > lines) ? data[i].length : lines;
      }
      for(int i=0; i<lines; i++) {
         sb.append("| ");
         for(int j=0; j<data.length; j++) {
            sb.append(
                  (data[j].length>i
                  ? data[j][i]
                  : makePadding(' ', width[j]))
                  + " | ");
         }
         sb.append("\n");
      }
      return sb.toString();
   }

   private String padText(String text, int length, int align) {
      text = text.trim();
      if(text.length() > length) {
         return text.substring(0, length);
      } else if(text.length() == length) {
         return text;
      } else {

         String aL=null, aR=null;
         
         switch(align) {
            case ALIGN_LEFT:
               aR = makePadding(' ', length - text.length());
               aL = "";
               break;
            case ALIGN_MIDDLE:
               int pad = length - text.length();
               int lp = pad/2;
               int rp = pad-lp;
               aL = makePadding(' ', lp);
               aR = makePadding(' ', rp);
               break;
            case ALIGN_RIGHT:
               aL = makePadding(' ', length - text.length());
               aR = "";
               break;
            default:
               throw new IllegalArgumentException("No such alignment type.");
         }

         return aL + text + aR;
      }
   }

   public String[] wordWrap(String text, int length, int align) {

      if(text.length() < length) 
         return new String[] { padText(text, length, align) };

      Vector lines = new Vector();
      StringBuffer line = new StringBuffer();
      StringTokenizer st = new StringTokenizer(text, " ");

      String word = st.nextToken();
      while(word != null) {
         int wlen = word.length();
         int llen = line.length();
         int sum = wlen + llen;
         int space;
         if(llen == 0) { // first word on line

            space = length-wlen;

            if(space == 0 || space == 1) { // only this word fits
               line.append(word);
               lines.add(line.toString());
               line = new StringBuffer();
               word = st.hasMoreTokens() ? st.nextToken() : null;
            } else if(space > 1) {
               line.append(word+" ");
               word = st.hasMoreTokens() ? st.nextToken() : null;
            } else { // word is longer than length
               line.append(word.substring(0, length));
               lines.add(line.toString());
               line = new StringBuffer();
               word = word.substring(length);
            }
         } else { // line already has words

            space = length-sum;
            if(space == 0 || space == 1) {
               line.append(word);
               lines.add(line.toString());
               line = new StringBuffer();
               word = st.hasMoreTokens() ? st.nextToken() : null;
            } else if(space > 1) {
               line.append(word+" ");
               word = st.hasMoreTokens() ? st.nextToken() : null;
            } else { 
               lines.add(line.toString());
               line = new StringBuffer();
            }

         }
      }
      if(line.length() > 0) lines.add(line.toString());
 
      String[] data = (String[]) lines.toArray(new String[0]);
      for(int i=0; i<data.length; i++) {
         data[i] = padText(data[i], length, align);
      }

      return data;
   }

   private String makePadding(int ch, int len) {
      StringBuffer sb = new StringBuffer();
      for(int i=len; i>0; i--) {
         sb.append((char) ch);
      }
      return sb.toString();
   }

   public static void main(String args[]) {
      Table t = new Table();
      t.addHeader("Erno", 20, ALIGN_MIDDLE);
      t.addHeader("%", 3, ALIGN_MIDDLE);

      t.addRow();
      t.addColumn("tämä teksti on liian pitkä ja se rivitetään sopimaan"+
            " koko systeemeihin ja muuta mahtavaa boonusta", 10, ALIGN_MIDDLE, true);
      t.addColumn("12", 3, ALIGN_RIGHT);
      t.addColumn("crappi", 7, ALIGN_MIDDLE);

      t.addRow();
      t.addColumn("Tekstiä myös", 20, ALIGN_MIDDLE, true);
      t.addColumn("85", 3, ALIGN_LEFT);
      
      System.out.println(t.render());
   }
}
