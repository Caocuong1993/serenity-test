package api.blog;

import common.BaseTest;
import common.db.blog.BlogSimple;
import features.api.blog.Authen;
import features.api.blog.UserInfo;
import io.restassured.response.Response;
import models.features.blog.Blog;
import models.features.blog.Token;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class BlogTest extends BaseTest {

    private static String username;
    private static final Authen authenApi = new Authen();
    private static final UserInfo userInfoApi = new UserInfo();
    private static models.features.blog.UserInfo userInfoModel = new models.features.blog.UserInfo();
    private static models.features.blog.Authen authenModel = new models.features.blog.Authen();
    private static final BlogSimple blogSimple = new BlogSimple();
    private static final Blog blogModel = new Blog();
    private static final features.api.blog.Blog blogApi = new features.api.blog.Blog();
    private static String token;

    private static final String title = "Test Automation using Serenity";
    private static final String content = "A lot of things man";
    private static final String author = "Donald";

    @BeforeClass
    public static void setUp(){

        username = RandomStringUtils.randomAlphabetic(8) + "@coccoc.com";
        String password = RandomStringUtils.randomAlphabetic(4);
        userInfoModel.setFullname(RandomStringUtils.randomAlphabetic(4) + " " + RandomStringUtils.randomAlphabetic(5));
        userInfoModel.setUsername(username);
        userInfoModel.setPassword(password);
        userInfoApi.createUser(userInfoModel);

        authenModel.setUsername(username);
        authenModel.setPassword(password);

        Response response = authenApi.authenUser(authenModel);
        Token actualResponse = response.as(Token.class);

        token = actualResponse.getToken();


    }

    @Test
    public void create_new_blog(){

        blogModel.setTitle(title);
        blogModel.setContent(content);
        blogModel.setAuthor(author);
        Response responseBlog = blogApi.createBlog(blogModel,token);
        Blog responseBlogActual = responseBlog.as(Blog.class);
        softAssertImpl.assertThat("Verify status code is 200",responseBlog.getStatusCode(), 200);
        softAssertImpl.assertThat("Verify title is correct",responseBlogActual.getTitle(),title);
        softAssertImpl.assertThat("Verify content is correct", responseBlogActual.getContent(), content);
        softAssertImpl.assertThat("Verify id is not null",responseBlogActual.getId().toString().isEmpty(),false);

    }

    @Test
    public void get_blog_by_id(){
        blogModel.setTitle(title);
        blogModel.setContent(content);
        blogModel.setAuthor(author);
        Response responseBlog = blogApi.createBlog(blogModel,token);
        Blog responseBlogActual = responseBlog.as(Blog.class);

        Response responseBlogById = blogApi.getBlogById(blogModel, token, responseBlogActual.getId().toString());
        Blog responseBlogActualById = responseBlogById.as(Blog.class);

        softAssertImpl.assertThat("Verify status code is 200",responseBlogById.getStatusCode(), 200);
        softAssertImpl.assertThat("Verify title is correct",responseBlogActualById.getTitle(),title);
        softAssertImpl.assertThat("Verify content is correct", responseBlogActualById.getContent(), content);
        blogSimple.clearUserInfoByUsername(username);
        blogSimple.clearBlogByTitle(title);
        softAssertImpl.assertAll();

    }

    @AfterClass
    public static void tearDown(){

        blogSimple.clearUserInfoByUsername(username);
        blogSimple.clearBlogByTitle(title);
        softAssertImpl.assertAll();

    }

}
