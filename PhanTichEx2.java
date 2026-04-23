package com.ss14;

public class PhanTichEx2 {
//# Phân tích Vấn đề Transaction trong Hibernate
//
//## Phần 1 - Phân tích
//
//### Tại sao đơn hàng vẫn bị đổi trạng thái khi "Hoàn kho" bị lỗi?
//
//Khi không có Transaction, Hibernate hoạt động ở chế độ **auto-commit** ngầm định. Điều này có nghĩa là:
//
//1. **Mỗi câu lệnh SQL được thực thi ngay lập tức và không thể quay lui**
//2. `session.update(order)` thực thi câu lệnh UPDATE ngay lập tức và commit vào database
//3. Khi `order.getProductId()` trả về null, gây ra lỗi ở bước "Hoàn kho"
//4. Lỗi này chỉ ảnh hưởng đến câu lệnh hiện tại, không ảnh hưởng đến các câu lệnh đã thực hiện trước đó
//5. Kết quả: Đơn hàng đã được cập nhật trạng thái thành "CANCELLED" nhưng kho không được hoàn trả
//
//### Thiệt hại đối với kho hàng
//
//1. **Mất cân bằng kho**: Sản phẩm đã được trừ khỏi kho khi đặt hàng, nhưng không được hoàn trả khi hủy đơn
//2. **Sai lệch số liệu**: Báo cáo tồn kho không phản ánh đúng thực tế
//3. **Ảnh hưởng kinh doanh**:
//- Khách hàng không thể đặt hàng sản phẩm đã hết hàng (do hệ thống cho rằng còn hàng)
//- Mất doanh thu do sai lệch tồn kho
//4. **Vấn đề vận hành**: Phải thực hiện kiểm kê và điều chỉnh thủ công để cân bằng lại kho
//
//## Phần 2 - Giải pháp
//
//### Transaction là gì?
//
//Transaction là một đơn vị công việc nguyên tử (atomic), đảm bảo:
//- **Atomicity**: Tất cả các thao tác thành công hoặc tất cả đều thất bại
//- **Consistency**: Dữ liệu luôn ở trạng thái nhất quán
//- **Isolation**: Các transaction không ảnh hưởng lẫn nhau
//- **Durability**: Dữ liệu được lưu trữ vĩnh viễn khi commit
//
//### Cách triển khai trong Hibernate
//
//1. **Bắt đầu Transaction**: `session.beginTransaction()`
//2. **Thực hiện các thao tác**: Các câu lệnh DML (update, insert, delete)
//3. **Commit Transaction**: `transaction.commit()` khi tất cả thành công
//4. **Rollback Transaction**: `transaction.rollback()` khi có lỗi
//
//### Lợi ích của Transaction
//
//1. **Đảm bảo tính toàn vẹn dữ liệu**: Không có trạng thái trung gian
//2. **Khả năng phục hồi**: Có thể quay lui khi có lỗi
//3. **An toàn dữ liệu**: Ngăn chặn dữ liệu không nhất quán
//4. **Dễ dàng gỡ lỗi**: Lỗi được phát hiện sớm và không gây ảnh hưởng lan rộng
}
