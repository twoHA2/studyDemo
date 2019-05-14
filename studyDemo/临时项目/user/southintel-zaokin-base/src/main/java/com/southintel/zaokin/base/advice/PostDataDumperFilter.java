package com.southintel.zaokin.base.advice;


import com.southintel.zaokin.base.util.BufferHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Auther: xumeng
 * @Date: 2018/5/9/009 16:14
 * @Description: 对request请求进行包装，使之成为可以重复读取ServerletInputStream的请求
 */
@Slf4j
public class PostDataDumperFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 备份HttpServletRequest
		ServletRequest requestWrapper = null;
		if (request instanceof HttpServletRequest) {
			requestWrapper = new BufferHttpServletRequestWrapper((HttpServletRequest) request);

			StringBuffer sb = new StringBuffer();
			InputStream is = requestWrapper.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			is.close();
			isr.close();
			br.close();
			String result = sb.toString();
			String url = ((HttpServletRequest) request).getServletPath();
			request.setAttribute("queryString", result);
			/*String token = interceptToken(result);
			if (StringUtils.isNotEmpty(token)) {
				log.info("地址{},json={},customer{}", url, result, CacheUtil.getCustomerByToken(token));
			} else {*/
				log.info("地址{},json={}", url, result);
//			}

		}
		if (null == requestWrapper) {
			chain.doFilter(request, response);
		} else {
			chain.doFilter(requestWrapper, response);
		}
	}

	@Override
	public void destroy() {

	}

	/*private static String interceptToken(String json) {
		if (json.contains("token")) {
			String afterSubStringToken = json.substring(json.indexOf("token") + "token".length());
			String remain;
			if(afterSubStringToken.indexOf(",") > afterSubStringToken.indexOf("}")) {
				remain = afterSubStringToken.substring(0,afterSubStringToken.indexOf("}"))
			}
			return remain.substring(remain.indexOf(":") + 1, remain.indexOf("}")).trim().replaceAll("\"", "").replaceAll("'", "");
		}
		return "";
	}*/

	/*public static void main(String[] args) {
		System.out.println(interceptToken("{\"token\":\"12345\"    \n  ,\"abc\":\"456\"}"));
	}*/
}
