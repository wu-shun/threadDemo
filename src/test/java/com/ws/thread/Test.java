package com.ws.thread;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

/**
 * @Copyright: Copyright (c) 兆日科技股份有限公司
 * @Date 2022/3/13 16:10
 * @Des
 */
public class Test {

    @org.junit.Test
    public void test1() throws ExecutionException, InterruptedException {

//      1:使用lamda传递了一个Runable对象
        new Thread(
                () ->System.out.println("线程启动")
        ).start();

//      创建一个FutureTask 实例对象,并将其传递给 Thread
        FutureTask<Integer> task = new FutureTask<Integer>(
                /*new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        System.out.println("启动");
                        return 111;
                    }
                }*/
                () -> {
                    System.out.println("启动");
                    Thread.sleep(5000);
                    return 111;
                }
        );

//        传递一个furureTask对象
        Thread thread = new Thread(task,"t1");
        thread.start();
        thread.getState();
//        如果程序(run方法)没有执行完,主线程会一直等待返回结果值
        Integer integer = task.get();
        System.out.println(integer);

    }


    @org.junit.Test
    public void testtree(){
        //模拟从数据库查询出来
        List<Menu> menus = Arrays.asList(
                new Menu(1,"0",0),
                new Menu(2,"1",1),
                new Menu(3,"1.1",2),
                new Menu(4,"1.2",2),
                new Menu(5,"1.3",2),
                new Menu(6,"2",1),
                new Menu(7,"2.1",6),
                new Menu(8,"2.2",6),
                new Menu(9,"2.2.1",8),
                new Menu(10,"2.2.2",8),
                new Menu(11,"3",1),
                new Menu(12,"3.1",11)
        );

        //获取父节点
        List<Menu> collect = menus.stream().filter(m -> m.getParentId() == 0).map(
                (m) -> {
                    m.setChildList(getChildrens(m, menus));
                    return m;
                }
        ).collect(Collectors.toList());
        System.out.println("-------转json输出结果-------");
//        System.out.println(JSON.toJSON(collect));
    }

    /**
     * 递归查询子节点
     * @param root  根节点
     * @param all   所有节点
     * @return 根节点信息
     */
    private List<Menu> getChildrens(Menu root, List<Menu> all) {
        List<Menu> children = all.stream().filter(m -> {
            return Objects.equals(m.getParentId(), root.getId());
        }).map(
                (m) -> {
                    m.setChildList(getChildrens(m, all));
                    return m;
                }
        ).collect(Collectors.toList());
        return children;
    }



}


class Menu {
    /**
     * id
     */
    public Integer id;
    /**
     * 名称
     */
    public String name;
    /**
     * 父id ，根节点为0
     */
    public Integer parentId;
    /**
     * 子节点信息
     */
    public List<Menu> childList;


    public Menu(Integer id, String name, Integer parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public Menu(Integer id, String name, Integer parentId, List<Menu> childList) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.childList = childList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<Menu> getChildList() {
        return childList;
    }

    public void setChildList(List<Menu> childList) {
        this.childList = childList;
    }
}