package com.company.kotlinapp_apis.service.impl.shop;

import com.company.kotlinapp_apis.dao.shop.ShopRepository;
import com.company.kotlinapp_apis.dto.shop.ShopDto;
import com.company.kotlinapp_apis.model.shop.Shop;
import com.company.kotlinapp_apis.service.inter.shop.ShopServiceInter;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopServiceInter {

    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;

    public ShopServiceImpl(ShopRepository shopRepository, ModelMapper modelMapper) {
        this.shopRepository = shopRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<ShopDto> createShop(ShopDto shopDto) {
        Shop shopEntity = modelMapper.map(shopDto, Shop.class);
        if(isShopEmailTaken(shopEntity.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        shopEntity.setDisable(true);
        Shop savedShop = shopRepository.save(shopEntity);
        return ResponseEntity.ok(modelMapper.map(savedShop, ShopDto.class));
    }

    @Override
    public List<ShopDto> getAllShops() {
        List<ShopDto> listDto = new ArrayList<>();

        List<Shop> listEntity = shopRepository.findAll();

        for (Shop entity : listEntity) {
            ShopDto dto = modelMapper.map(entity, ShopDto.class);
            listDto.add(dto);
        }

        return listDto;
    }

    @Override
    public ShopDto getShopById(Long id) {
        Shop entity = shopRepository.findById(id).orElseGet(() -> null);
        if (entity != null) {
            return modelMapper.map(entity, ShopDto.class);
        }
        return null;
    }

    @Override
    public Boolean isShopEmailTaken(String email) {
        return shopRepository.findShopByEmail(email) != null;
    }

    @Override
    public ShopDto findShopByEmail(String email) {
        Shop shop = shopRepository.findShopByEmail(email);
        if(shop != null) {
            return modelMapper.map(shop, ShopDto.class);
        }
        return null;
    }

    @Override
    public ShopDto updateShop(Long id, ShopDto shopDto) {
        Shop existingShop = shopRepository.findById(id).orElseThrow(() ->
                new UnsupportedOperationException("Shop not found with id:" + id));
        modelMapper.map(shopDto, existingShop);
        Shop updatedShop = shopRepository.save(existingShop);
        return modelMapper.map(updatedShop, ShopDto.class);
    }

    @Override
    public void deleteShopById(Long id) {
        shopRepository.deleteById(id);
    }
}
