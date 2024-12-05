package ro.tuc.ds2020.dtos;

import java.util.UUID;

public class EmailDTO {

        private UUID userId;

        public EmailDTO() {
        }

        public EmailDTO(UUID userId) {
            this.userId = userId;
        }

        public UUID getUserId() {
            return userId;
        }

        public void setUserId(UUID userId) {
            this.userId = userId;
        }

}
