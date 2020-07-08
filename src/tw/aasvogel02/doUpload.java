package tw.aasvogel02;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/doUpload")
@MultipartConfig//←這個也恨重要，注意一下!!
public class doUpload extends HttpServlet {

	protected void doPost(HttpServletRequest request
			, HttpServletResponse response) 
					throws ServletException, IOException {
		
		Part upload = request.getPart("upload");
		//↓箭頭之間的很重要  要記得
		ServletContext context = getServletContext();
		String storagePath = context.getInitParameter("outsideStoragePath");
		System.out.println(storagePath);//儲存路徑
		//↑箭頭之間的很重要  要記得
		
		String filename =upload.getSubmittedFileName();//檔案名稱
		System.out.println(filename);
		
		long size = upload.getSize();
		String filesize = Long.toString(size);//檔案大小
		System.out.println(filesize);
		
		
		String downUrl = "/downUrl/";//
		String folder = downUrl+filename;
		System.out.println(folder);//下載路徑
		
		Date date = new Date();
		DateFormat shortFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);	
		System.out.println(shortFormat.format(date));//上傳時間
		
		BufferedInputStream bin = new BufferedInputStream(upload.getInputStream());
		byte [] buf = new byte [(int)size];
		bin.read(buf);
		bin.close();
		
		File uploadFile = new File(storagePath,filename);
		FileOutputStream fout =new FileOutputStream(uploadFile);
		fout.write(buf);
		fout.flush();
		fout.close();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");//PS:載入MYSQL驅動程式
			
			Properties prop = new Properties();
			prop.put("user", "user");
			prop.put("password", "password");
			prop.put("serverTimezone", "Asia/Taipei");
			Connection conn =DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/forjavaee", prop);
			String sql = "INSERT INTO secondproject (filename,filesize,fileaddress,uploaddate,fordown) VALUE(?,?,?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, filename);
			pstmt.setString(2, filesize);
			pstmt.setString(3, storagePath);
			pstmt.setString(4, shortFormat.format(date));
			pstmt.setString(5, folder);
			
			pstmt.executeUpdate();
			
			System.out.println("OK");
			
			conn.close();
			
			response.sendRedirect("FileList.jsp");
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}

}
