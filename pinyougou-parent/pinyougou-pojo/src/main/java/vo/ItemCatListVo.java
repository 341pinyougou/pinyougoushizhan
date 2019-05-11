package vo;

import cn.itcast.core.pojo.item.ItemCat;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
;

public   class   ItemCatListVo implements Serializable {
    private ItemCat list2;
    private List<ItemCat> list3;

    @Override
    public String toString() {
        return "ItemCatListVo{" +
                "list2=" + list2 +
                ", list3=" + list3 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCatListVo that = (ItemCatListVo) o;
        return Objects.equals(list2, that.list2) &&
                Objects.equals(list3, that.list3);
    }

    @Override
    public int hashCode() {

        return Objects.hash(list2, list3);
    }

    public ItemCat getList2() {

        return list2;
    }

    public void setList2(ItemCat list2) {
        this.list2 = list2;
    }

    public List<ItemCat> getList3() {
        return list3;
    }

    public void setList3(List<ItemCat> list3) {
        this.list3 = list3;
    }
}
