
package com.ly.imart.maxim.message.contract;

import com.ly.imart.maxim.common.base.IBasePresenter;
import com.ly.imart.maxim.common.base.IBaseView;

/**
 * Description : 会话contract Created by Mango on 2018/11/05.
 */
public interface SessionContract {

    /**
     * 会话view
     */
    interface View extends IBaseView<Presenter> {

    }

    /**
     * 会话presenter
     */
    interface Presenter extends IBasePresenter<View> {

    }

    /**
     * 会话model
     */
    interface Model {
    }
}
