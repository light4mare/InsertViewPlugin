# InsertViewPlugin
使用gradle脚本重写的ButterKnife,在gradle编译过程中根据注解插入、生成相应的字节码以达到与ButterKnife相同的作用

使用方法跟ButterKnife没什么区别,
支持多个View绑定到List中
支持方法绑定

原理：
View绑定使用ASM直接在原文件内注入
方法绑定使用ASM生成新的OnClickListener类，并在原文件内引用

如果错误地使用了注解会在run窗口输出错误信息，但是可以正常运行

public class MainActivity extends BaseActivity {
    @BindView(R.id.linear)
    private LinearLayout linear;
    @BindView(R.id.head)
    private TextView head;
    @BindView(R.id.button)
    private Button button;
    @BindView(R.id.middle)
    private TextView middle;
    @BindView(R.id.image)
    private ImageView image;

    @BindViews({R.id.linear, R.id.head, R.id.button, R.id.image})
    private List<View> viewList;

    @BindView(R.id.view_list)
    private TextView view_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InsertUtil.bind(this);
    }
}

给gradle插件新手的提示
1.如果修改了脚本，把应用插件的build目录删掉，重新上传脚本，执行transformClassesWithViewInsertTransformForDebug可以看到是否成功
2.脚本所修改的文件存在于Project\app\build\intermediates\transforms\ViewInsertTransform\debug\folders\1\1\insertView
3.如果脚本无法上传(uploadArchive),多打开一个项目再试试（不要问我为什么）
4.脚本的println输出在run窗口可以看到，如果没看到，点一下该窗口左上角的ab按钮可以切换text模式