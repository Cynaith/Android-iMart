package com.ly.imart.presenter.Login;

import com.ly.imart.bean.Login.PersonalDataBean;
import com.ly.imart.model.Login.IPersonalDataModel;
import com.ly.imart.model.Login.PersonalDataModelImpl;
import com.ly.imart.view.Login.IPersonalDataView;

public class PersonalDataPresenter {
    private static final String ACTIVITY_TAG = "PersonalDataPresenter";

    private PersonalDataBean personalDataBean = new PersonalDataBean();
    private IPersonalDataView iPersonalDataView;
    private IPersonalDataModel iPersonalDataModel;

    public PersonalDataPresenter(IPersonalDataView iPersonalDataView) {
        this.iPersonalDataView = iPersonalDataView;
        iPersonalDataModel = new PersonalDataModelImpl();
    }

    public boolean commit(){
        iPersonalDataModel.savePersonalData(iPersonalDataView.getName(),iPersonalDataView.getMail(),iPersonalDataView.getAge());
        if(true){
//            iPersonalDataView.goHome();
//            提交数据至后台
        }
        else{
            //可以设置弹框验证格式，并set属性为空
        }
        return true;
    }

}
