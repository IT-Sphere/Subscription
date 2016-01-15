/**
 * http://it-channel.ru/
 *
 * Table for subscription
 *
 * @author Budnikov Aleksandr
 */
CREATE TABLE subscription
(
  id BIGINT AUTO_INCREMENT,
  name VARCHAR(255),
  clientId BIGINT,
  organizationId BIGINT,
  PRIMARY KEY (id),
  FOREIGN KEY (clientId) REFERENCES client(id),
  FOREIGN KEY (organizationId) REFERENCES organization(id)
);
