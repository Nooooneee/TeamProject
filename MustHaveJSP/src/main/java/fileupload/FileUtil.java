package fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class FileUtil
{
	//파일 업로드
	public static String uploadFile(HttpServletRequest req, String sDirectory) 
			throws ServletException, IOException {
		//Part 객체를 통해 서버로 전송된 파일명 읽어오기 
		Part part = req.getPart("ofile");					
		 
		//Part 객체의 헤더값 중 content-disposition 읽어오기 
        String partHeader = part.getHeader("content-disposition");
        //출력결과 => form-data; name="attachedFile"; filename="파일명.jpg"
        System.out.println("partHeader="+ partHeader);
         
        //헤더값에서 파일명 잘라내기
        String[] phArr = partHeader.split("filename=");
        String originalFileName = phArr[1].trim().replace("\"", "");
		
		//전송된 파일이 있다면 디렉토리에 저장
		if (!originalFileName.isEmpty()) {				
			part.write(sDirectory+ File.separator +originalFileName);
		}
		 
		//원본 파일명 반환
		return originalFileName;			
	}
	
	//파일명 변경
	public static String renameFile(String sDirectory, String fileName) {
		// 원본파일의 확장자 잘라내기
		String ext = fileName.substring(fileName.lastIndexOf("."));
		// 날짜 및 시간을 통해 "현재날짜_시간" 형식의 문자열을 생성
		String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date());
		// "날짜_시간.확장자" 형태의 새로운 파일명 생성
		String newFileName = now + ext;  

		// 객체를 생성 후 기존 파일명을 새로운 파일명으로 변경
		File oldFile = new File(sDirectory + File.separator + fileName);
	    File newFile = new File(sDirectory + File.separator + newFileName);
	    oldFile.renameTo(newFile);
	    
	    //변경된 파일명 반환
	    return newFileName;
	}
	
	// 14-17 파일 다운로드
	//  명시한 파일을 찾아 다운로드합니다.
	public static void download(HttpServletRequest req, HttpServletResponse resp,
            String directory, String sfileName, String ofileName) {
        String sDirectory = req.getServletContext().getRealPath(directory);	// 물리적 경로 디렉토리
        try {
            // 파일을 찾아 입력 스트림 생성
            File file = new File(sDirectory, sfileName);
            InputStream iStream = new FileInputStream(file);

            // 한글 파일명 깨짐 방지
            String client = req.getHeader("User-Agent");	// 웹브라우져 종류
            if (client.indexOf("WOW64") == -1) {	// 익스플로러
                ofileName = new String(ofileName.getBytes("UTF-8"), "ISO-8859-1");
            }
            else {
                ofileName = new String(ofileName.getBytes("KSC5601"), "ISO-8859-1");
            }

            // 파일 다운로드 응답 헤더 설정
            resp.reset();
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition",
                           "attachment; filename=\"" + ofileName + "\"");
            resp.setHeader("Content-Length", "" + file.length() );

            //out.clear();  // 출력 스트림 초기화. 서블릿에서는 예외가 발생하지 않아 주석처리
            
            // response 내장 객체로부터 새로운 출력 스크림 생성
            OutputStream oStream = resp.getOutputStream();

            // 출력 스크림에 파일 내용 출력
            byte b[] = new byte[(int)file.length()];
            int readBuffer = 0;
            while ( (readBuffer = iStream.read(b)) > 0 ) {
                oStream.write(b, 0, readBuffer);
            }

            // 입출력 스크림 닫음
            iStream.close();
            oStream.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다.");
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("예외가 발생하였습니다.");
            e.printStackTrace();
        }
    }
	
	// 파일 삭제
	// 파일 삭제를 위한 메서드
	public static void deleteFile(HttpServletRequest req,
            String directory, String filename) {
		// 물리적 경로와 파일명을 통해 File객체를 생성한다.
        String sDirectory = req.getServletContext().getRealPath(directory);
        File file = new File(sDirectory + File.separator + filename);
        // 해당 경로에 파일이 존재하면...
        if (file.exists()) {
        	// 파일을 삭제한다.
            file.delete();
        }
    }
}
