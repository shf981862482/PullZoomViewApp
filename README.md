# PullZoomViewApp

recyclerview   
```
        recyclerview.setZoomView(zoom_view);
        recyclerview.setHeaderContainer(zoom_container);
```


scrollview
```
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.scroll_detail, null);
        RelativeLayout zoom_container =
                (RelativeLayout) view.findViewById(R.id.zoom_container);
        ImageView zoom_view = (ImageView) view.findViewById(R.id.zoom_view);
        scrollView = (PullZoomScrollView) findViewById(R.id.scrollView);
        scrollView.addView(view);
        scrollView.setZoomView(zoom_view);
        scrollView.setHeaderContainer(zoom_container);
```
<img src="https://pan.baidu.com/disk/timeline" />
