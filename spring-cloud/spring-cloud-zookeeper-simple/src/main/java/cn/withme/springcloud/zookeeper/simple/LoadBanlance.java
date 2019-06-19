/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月18日
 */
package cn.withme.springcloud.zookeeper.simple;


import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.zookeeper.common.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * ClassName: LoadBanlance
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月18日
 */
public class LoadBanlance implements ClientHttpRequestInterceptor {

//    @Autowired
//    private DiscoveryClient client;

    @Autowired
    ServerInfo serverInfo;

    @Autowired
    Task task;

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        URI uri = httpRequest.getURI();
        //"/" + servierName + "/hello"
        //servierName
        String serverName = uri.getPath().split("/")[1];
        //HttpMethod httpRequestMethod = httpRequest.getMethod();
        //hello
        String serverMethod = uri.getPath().split("/")[2];


        //获得zookeeper 注册列表(可以传入服务名,这样就能把这个项目所对应的集群节点ip 全部拿到,然后就可以自己模拟负载均衡了)
        Map<String, Set<String>> serversMap = serverInfo.getServersMap();

        if (MapUtils.isEmpty(serversMap) || null == serversMap.get(serverName)) {
            task.getServerList();//可能任务调度还没有执行,主动执行一次任务调度
        }
        Object[] targetServerNames = serversMap.get(serverName).toArray();
        String randomServer = (String) targetServerNames[new Random().nextInt(targetServerNames.length)];
        String targetUrl = randomServer + "/" + serverMethod + (uri.getQuery()==null?"":uri.getQuery());
        URL url = new URL(targetUrl);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<InputStream> responseEntity = restTemplate.getForEntity(, InputStream.class);
        //byte[] responseByte = org.apache.commons.io.IOUtils.toByteArray(url.openConnection().getInputStream());
        return new ClientHttpResponseImpl(new HttpHeaders(), url.openConnection().getInputStream());
    }

    class ClientHttpResponseImpl implements ClientHttpResponse {
        private InputStream inputStream;

        private HttpHeaders httpHeaders;

        public ClientHttpResponseImpl(HttpHeaders httpHeaders, InputStream inputStream) {
            this.inputStream = inputStream;
            this.httpHeaders = httpHeaders;
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return HttpStatus.OK;
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return 200;
        }

        @Override
        public String getStatusText() throws IOException {
            return "success";
        }

        @Override
        public void close() {

        }

        @Override
        public InputStream getBody() throws IOException {
            return inputStream;
        }

        @Override
        public HttpHeaders getHeaders() {
            return httpHeaders;
        }
    }


}
