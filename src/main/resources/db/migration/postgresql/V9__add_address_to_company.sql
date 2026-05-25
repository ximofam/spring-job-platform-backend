ALTER TABLE companies
ADD COLUMN address_id BIGINT;


ALTER TABLE companies 
ADD CONSTRAINT fk_companies_address 
FOREIGN KEY (address_id) REFERENCES addresses (id) ON DELETE SET NULL;

CREATE INDEX idx_companies_address_id ON companies(address_id);