package features.api.blog;

import common.ApiUtil;
import common.CoreConstant;
import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.given;

public class Blog {

    public Response createBlog(models.features.blog.Blog blog, String token){

        return given().baseUri(CoreConstant.BLOG_HOST + CoreConstant.BLOG_API_BLOG)
                .header("Content-Type", "application/json")
                .header("Authorization",token)
                .body(ApiUtil.parseObjectToJSON(blog, models.features.blog.Blog.class))
                .when()
                .post();

    }

    public Response getBlogById(models.features.blog.Blog blog, String token, String blogId){

        return given().baseUri(CoreConstant.BLOG_HOST + CoreConstant.BLOG_API_BLOG +"/"+blogId)
                .header("Content-Type", "application/json")
                .header("Authorization",token)
                .body(ApiUtil.parseObjectToJSON(blog, models.features.blog.Blog.class))
                .when()
                .get();
    }
}
