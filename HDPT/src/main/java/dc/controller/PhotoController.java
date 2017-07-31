package dc.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bsoft.mpi.server.MPIServiceException;
import com.bsoft.mpi.server.rpc.IMPIProvider;

import dc.image.IImageLoader;

@Controller
public class PhotoController {
	@Autowired
	private IImageLoader httpImageLoader;
	@Autowired
	private IMPIProvider mpiService;
	
	/**
	 * 获取mpi照片
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/personalMPIPhoto")
	public void personalMPIPhoto(HttpServletRequest request,
			HttpServletResponse response) {
		String PhotosUrl ="";
			try {
				PhotosUrl = mpiService.getPhotosUrl((String) request.getSession().getAttribute("mpiId"));
			} catch (MPIServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		response.setContentType(IImageLoader.responseContentType);
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			if (false == httpImageLoader.load(PhotosUrl, out)) {
				out.write("0".getBytes());
//				response.sendError(404);
			}
		} catch (IOException e) {

		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取社区照片
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/personalSQPhoto")
	public void personalSQPhoto(HttpServletRequest request,
			HttpServletResponse response) {
		String 	PhotosUrl = "http://192.26.28.44:8081/HZEHR/image/"+request.getSession().getAttribute("idcard")+".jpeg";
		response.setContentType(IImageLoader.responseContentType);
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			if (false == httpImageLoader.load(PhotosUrl, out)) {
				out.write("0".getBytes());
//				response.sendError(404);
			}
		} catch (IOException e) {

		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
