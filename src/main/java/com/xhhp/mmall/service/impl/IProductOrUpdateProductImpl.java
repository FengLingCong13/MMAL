package com.xhhp.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xhhp.mmall.common.ResponseCode;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.dao.CategoryMapper;
import com.xhhp.mmall.dao.ProductMapper;
import com.xhhp.mmall.pojo.Category;
import com.xhhp.mmall.pojo.Product;
import com.xhhp.mmall.service.IProductService;
import com.xhhp.mmall.util.DateTimeUtil;
import com.xhhp.mmall.vo.ProductDetailVo;
import com.xhhp.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * IProductOrUpdateProductImpl class
 *
 * @author Flc
 * @date 2019/9/27
 */
@Service(value = "iProductService")
@PropertySource(value = {"classpath:/application-${spring.profiles.active}.properties"})
public class IProductOrUpdateProductImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Value("${ftp.server.http.prefix}")
    private String ftpPrefix;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {

        if(product != null) {
            if(StringUtils.isNoneBlank(product.getSubImages())) {
                //取第一个子图赋值给主图
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }

            if(product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount > 0) {
                    return ServerResponse.createBySuccess("更新产品成功");
                } else {
                    return ServerResponse.createByERRORMessage("更新产品失败");
                }

            } else {
                int rowCount = productMapper.insert(product);
                if(rowCount > 0) {
                    return ServerResponse.createBySuccess("新增产品成功");
                } else {
                    return ServerResponse.createByERRORMessage("新增产品失败");
                }

            }
        } else {
            return ServerResponse.createByERRORMessage("新增或更新产品参数不正确");
        }
    }

    @Override
    public ServerResponse setSaleStatus(int productId, int status) {
        if(productId < 0 || status <0) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        int rowCount = productMapper.updateByPrimaryKeySelective(product);

        if(rowCount > 0) {
            return ServerResponse.createBySuccess("修改产品状态成功");
        }

        return ServerResponse.createByERRORMessage("修改产品状态失败");
    }

    @Override
    public ServerResponse manageProductDetail(int productId) {

        if(productId < 0) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }

        Product product = productMapper.selectByPrimaryKey(productId);

        if(product == null) {
            return ServerResponse.createByERRORMessage("产品已下架或者已删除");
        }
        ProductDetailVo productListVo = assembleproductDeatilVo(product);

        return ServerResponse.createBySuccess(productListVo);
    }

    @Override
    public ServerResponse getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageNum);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVos = Lists.newArrayList();
        for(Product productItem :productList) {
            System.out.println(productItem);
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVos.add(productListVo);
        }
        System.out.println(productList.size());
        System.out.println(productListVos.size());
        PageInfo pageResult = new PageInfo(productListVos);
        //pageResult.setList(productListVos);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse searchProduct(String productName, int productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNoneBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();

        }

        List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVos = Lists.newArrayList();
        for(Product productItem :productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVos.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVos);
        return ServerResponse.createBySuccess(productListVos);
    }

    private ProductDetailVo assembleproductDeatilVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setSubstitle(product.getSubtitle());

        productDetailVo.setImageHost(ftpPrefix);

        Category category =categoryMapper.selectByPrimaryKey(product.getCategoryId());

        if(category == null) {
            productDetailVo.setParentCategoryId(0);//默认根节点
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));

        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setImageHost(ftpPrefix);
        productListVo.setMainImage(product.getMainImage());

        return productListVo;
    }
}
