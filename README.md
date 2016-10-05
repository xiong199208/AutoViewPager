###实现的需求：

 1. 左右都可以无限滑动，小红点实时跟随。
 2. 2秒钟自动切换图片。
 3. 按下停止自动切换，放手继续切换。


----------


###细节处理：
 1. ViewPager显示的图片不再跟着集合走，所以显示第一张图片时，需要有一定的偏移量。
 2. ViewPager的循环播放印象，小红点在左右边界需要特殊指定。
     - 由于onPageScrolled的特性，左滑时position不变，右滑时position减1，所以position需要从onPageSelected取，再参与边界判断。
 3. 使用Handler的postDelay方法+run方法的递归实现自动切换
 4. 监听ViewPager的触摸事件，down时停止切换，up时开始切换，不能拦截事件（return true），不然无法滑动轮播图。


----------


###碰到的问题：

传入Adapter的是ImageView的集合的话，在instantiateItem方法里会报The specified child already has a parent. You must call removeView() on the child's parent first.错误，加上：
```
if (imageView.getParent()==null) {
            container.addView(imageView);
        }
```
的判断，又无法实现无限切换视图。
