package com.example.librarymanagement.domain.dto.response.receipt;

import com.example.librarymanagement.domain.entity.CartDetail;
import com.example.librarymanagement.domain.entity.Reader;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BorrowRequestSummaryResponseDto {
    private long cartId;
    private String cartNumber;
    private String fullName;
    private List<String> borrowDocuments = new ArrayList<>();

    public void setCartDetails(List<CartDetail> filteredCartDetails){
        for(CartDetail cartDetail: filteredCartDetails){
            borrowDocuments.add(cartDetail.getBook().getBookCode());
        }
    }

    public void setReader(Reader reader){
        this.cartId = reader.getCart().getId();
        this.cartNumber = reader.getCardNumber();
        this.fullName = reader.getFullName();
    }
}
