/**
 * http://it-channel.ru/
 *
 * Table for client
 *
 * @author Budnikov Aleksandr
 */
CREATE TABLE client
(
  id BIGINT AUTO_INCREMENT,
  firstName VARCHAR(255),
  secondName VARCHAR(255),
  email VARCHAR(255),
  phone VARCHAR(255),
  PRIMARY KEY (id)
);
