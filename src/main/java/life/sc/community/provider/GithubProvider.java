package life.sc.community.provider;

import com.alibaba.fastjson.JSON;

import life.sc.community.dto.AccessTokenDTO;
import life.sc.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.alibaba.fastjson.JSON.parseObject;

@Component
public class GithubProvider {
    public String getAccesstoken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String s = response.body().string();
            String s_new = s.split("&")[0].split("=")[1];
            return s_new;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getuser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            GithubUser githubUser = parseObject(string, GithubUser.class);
            return githubUser;
        }catch (IOException e){
        }
        return null;
    }

}
