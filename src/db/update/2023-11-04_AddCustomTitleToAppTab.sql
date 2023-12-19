

-- Add Custom Title for Home Tag Management
ALTER TABLE app_tab ADD COLUMN custom_title VARCHAR(1024);
COMMENT ON COLUMN app_tab.custom_title IS '自定义标题';