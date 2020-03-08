
package com.ly.imart.maxim.common.bean;

import java.util.List;

public class PhotoViewListBean extends BaseBean {

    private List<PhotoViewBean> photoViewBeans;

    public List<PhotoViewBean> getPhotoViewBeans() {
        return photoViewBeans;
    }

    public void setPhotoViewBeans(List<PhotoViewBean> photoViewBeans) {
        this.photoViewBeans = photoViewBeans;
    }
}
