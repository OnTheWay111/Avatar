package tools.excelread;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 李振7
 * Created Time: 2017/11/9 下午4:38
 * 根据filePath,fileName,caseName，获取excel中的数据，保存为HashMap<String, String>[]
 */
@SuppressWarnings("ALL")
public class GetTestCaseExcel {
    public Workbook workbook;
    public Sheet sheet;
    public Cell cell;
    int rows;
    int columns;
    //文件路径
    public String filePath;
    //文件名，不包含文件后缀.xls
    public String fileName;
    //sheet名
    public String caseName;
    public ArrayList<String> arrkey = new ArrayList<String>();
    String sourceFile;

    /**
     * @param fileName   excel文件名
     * @param caseName   sheet名
     */
    public GetTestCaseExcel(String filePath, String fileName, String caseName) {
        super();
        this.fileName = fileName;
        this.caseName = caseName;
        this.filePath = filePath;
    }

    /**
     * 获得excel表中的数据
     */
    @SuppressWarnings("AlibabaCollectionInitShouldAssignCapacity")
    public Object[][] getExcelData() throws BiffException, IOException {

        workbook = Workbook.getWorkbook(new File(this.setPath(filePath,fileName)));
        sheet = workbook.getSheet(caseName);
        //获取该sheet行数
        rows = sheet.getRows();
        //获取该sheet列数
        columns = sheet.getColumns();
        // 为了返回值是Object[][],定义一个多行单列的二维数组
        HashMap<String, String>[][] arrmap = new HashMap[rows - 1][1];

        // 对数组中所有元素hashmap进行初始化
        if (rows > 1) {
            for (int i = 0; i < rows - 1; i++) {
                arrmap[i][0] = new HashMap<String, String>(100);
            }
        } else {
            System.out.println("excel中没有数据");
        }

        // 获得首行的列名，作为hashmap的key值
        for (int c = 0; c < columns; c++) {
            String cellvalue = sheet.getCell(c, 0).getContents();
            arrkey.add(cellvalue);
        }
        // 遍历所有的单元格的值添加到hashmap中
        for (int r = 1; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                String cellvalue = sheet.getCell(c, r).getContents();
                arrmap[r - 1][0].put(arrkey.get(c), cellvalue);
            }
        }
        return arrmap;
    }

    /**
     * 获得excel文件的路径
     * @return
     * @throws IOException
     * /Users/leeco/Desktop/letv/workspace/tools/src/resources/testData.xlsx
     */
    public String setPath(String path, String fileName) throws IOException {
        File directory = new File(".");
        sourceFile = directory.getCanonicalPath() + path + fileName + ".xls";
        return sourceFile;
    }

}