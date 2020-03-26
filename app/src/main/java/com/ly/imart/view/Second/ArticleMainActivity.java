package com.ly.imart.view.Second;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ly.imart.R;
import com.ly.imart.adapter.CommentExpandAdapter;
import com.ly.imart.bean.Second.ActionStatusBean;
import com.ly.imart.bean.Second.ArticleBean;
import com.ly.imart.bean.Second.ArticleMainBean;
import com.ly.imart.bean.Second.CommentBean;
import com.ly.imart.bean.Second.CommentDetailBean;
import com.ly.imart.bean.Second.ReplyDetailBean;
import com.ly.imart.model.Second.ArticleMainModelImpl;
import com.ly.imart.model.Second.ArticleMiddleModel;
import com.ly.imart.model.Second.ArticleModel;
import com.ly.imart.presenter.Second.ArticleMainPresenter;
import com.ly.imart.util.CircleImageView;
import com.ly.imart.util.MyImageView;
import com.ly.imart.view.Fourth.FourthMyshowActivity;
import com.varunest.sparkbutton.SparkButton;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Webview https://blog.csdn.net/LI_YU_CSDN/article/details/80490771
 */

public class ArticleMainActivity extends AppCompatActivity implements IArticleMainView ,View.OnClickListener{
    private static final String TAG = "ArticleMainActivity";
    private static int articleId = 1;
    private ArticleMainModelImpl articleMainModel;
    private ArticleMiddleModel articleMiddleModel;
    private CollapsingToolbarLayout collapsingToolbar;
    private android.support.v7.widget.Toolbar toolbar;
    private TextView bt_comment; //输入框
    private SparkButton supportButton;
    private SparkButton collectionButton;
    private boolean isSupport;
    private boolean isCollection;
    private TextView supportCount;
    private TextView collectionCount;
    private int supportNum;
    private int collectionNum;
    private TextView authorname;
    private TextView articletime;
    private CircleImageView userlogo;
    private MyImageView articleimg;
    private HtmlTextView htmlTextView;
    private String html;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList;
    private BottomSheetDialog dialog;
    private ArticleBean articleBean;

    public static void openArticleMainById(Context context, int id) {
        articleId = id;
        Intent intent = new Intent(context, ArticleMainActivity.class);
        context.startActivity(intent);
        //未完成：Article不见时finsh；
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_content);
        articleMainModel = new ArticleMainModelImpl();
        articleMiddleModel = new ArticleMiddleModel();
        initView();
        try {
            initData(articleId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        authorname = findViewById(R.id.detail_page_userName);
        supportButton = findViewById(R.id.article_support);
        collectionButton = findViewById(R.id.article_collection);
        supportButton.setOnClickListener(this);
        collectionButton.setOnClickListener(this);
        supportCount = findViewById(R.id.article_support_count);
        collectionCount = findViewById(R.id.article_collection_count);
        articletime = findViewById(R.id.detail_page_time);
        userlogo = findViewById(R.id.detail_page_userLogo);
        userlogo.setOnClickListener(this);
        articleimg = findViewById(R.id.detail_page_image);
        try {
            commentsList = generateTestData();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initExpandableListView(commentsList);
    }

    private void initData(int id) throws ExecutionException, InterruptedException {
        ArticleModel articleModel = new ArticleModel();
        articleBean = articleModel.getArticle(id);
        articleimg.setImageURL(articleBean.getImg1());
        collapsingToolbar.setTitle(articleBean.getTitle());
        authorname.setText(articleBean.getUsername());
        userlogo.setImageURL(articleBean.getUserImg());
        articletime.setText(articleBean.getTime());
        //html结合富文本框架
        htmlTextView = findViewById(R.id.article_html);
        html = articleBean.getText();
        html = html.replaceAll("src:", "src=");
        html = html.replaceAll("'", "\"");
        htmlTextView.setListIndentPx(20);
        htmlTextView.setHtml(html, new HtmlHttpImageGetter(htmlTextView,null,true));
        ActionStatusBean actionStatusBean = null;
        try {
            actionStatusBean= articleMiddleModel.actionStatus(articleId);
            isSupport = actionStatusBean.isSupport();
            isCollection = actionStatusBean.isCollection();
            supportNum = actionStatusBean.getSupportCount();
            collectionNum = actionStatusBean.getCollectionCount();
            supportCount.setText(""+supportNum);
            collectionCount.setText(""+collectionNum);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        supportButton.setChecked(isSupport);
        collectionButton.setChecked(isCollection);
    }

    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList){
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        for(int i = 0; i<commentList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentList.get(groupPosition).getId());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(ArticleMainActivity.this,"点击了回复",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }

    /**
     * func:获取评论
     * @return 评论数据
     */
    private List<CommentDetailBean> generateTestData() throws ExecutionException, InterruptedException {
        Gson gson = new Gson();
        List<CommentDetailBean> commentList = articleMainModel.getComment(articleId);
        return commentList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_page_userLogo:
                Intent intent = new Intent(this, FourthMyshowActivity.class);
                intent.putExtra("userName", articleBean.getUsername());
                startActivity(intent);
                break;
            case R.id.detail_page_do_comment:
                showCommentDialog();
                break;
            case R.id.article_collection:
                try {
                    articleMiddleModel.action(articleId,2);
                    if (!isCollection) {
                        collectionButton.playAnimation();
                        collectionNum++;
                    }
                    else {
                        collectionNum--;
                    }
                    collectionCount.setText(""+collectionNum);
                    collectionButton.setChecked(isCollection = !isCollection);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.article_support:
                try {
                    articleMiddleModel.action(articleId,1);
                    if (!isSupport){
                        supportButton.playAnimation();
                        supportNum++;
                    }
                    else {
                        supportNum--;
                    }
                    supportCount.setText(""+supportNum);
                    supportButton.setChecked(isSupport = !isSupport);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * by moos on 2018/04/20
     * func:弹出评论框
     */
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    CommentDetailBean detailBean = null;
                    try {
                        detailBean = articleMainModel.postComment(articleId,commentContent);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    adapter.addTheCommentData(detailBean);
                    Toast.makeText(ArticleMainActivity.this,"评论成功",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(ArticleMainActivity.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * by moos on 2018/04/20
     * func:弹出回复框
     */
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){
                    dialog.dismiss();
                    ReplyDetailBean detailBean = null;
                    try {
                        detailBean = articleMainModel.postReply(commentsList.get(position).getId(),replyContent);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    ReplyDetailBean detailBean = new ReplyDetailBean("小红",replyContent);
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                    Toast.makeText(ArticleMainActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ArticleMainActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }


}
