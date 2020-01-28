package com.ly.imart.model.Login;

import com.ly.imart.bean.Login.PersonalDataBean;
import com.ly.imart.view.Login.IPersonalDataView;

public class PersonalDataModelImpl implements IPersonalDataModel{

    private PersonalDataBean personalDataBean;

    public PersonalDataModelImpl() {
        personalDataBean = new PersonalDataBean();
    }

    @Override
    public void savePersonalData(String name ,String mail,int age) {
        personalDataBean.setNameAndMailAndAge(name,mail,age);
    }
}
