import {message, Modal, Space} from 'antd';
import {createUser} from "@/services/ant-design-pro/api";
import {ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {ProForm} from "@ant-design/pro-form";


interface CreateUserModalProps {
  isOpen: boolean,
  handleModalClose: () => void,
}

const CreateUserModal = ({isOpen, handleModalClose}: CreateUserModalProps) => {
  const handleSubmit = async (values: { userAccount: string; userRole: number }) => {
    const success = await createUser({userAccount: values.userAccount, userRole: values.userRole});
    if (success) {
      message.success('用户创建成功，初始密码为 password，请及时修改');
    } else {
      message.error('用户创建失败，请重试');
    }
  };

  return (
    <Modal
      title="Create User"
      open={isOpen}
      onCancel={handleModalClose}
      footer={null}
    >
      <ProForm
        layout="vertical"
        submitter={{
          render: (_, dom) => {
            return (
              <div style={{textAlign: 'right', marginRight: '10px'}}>
                <Space>{dom}</Space>
              </div>
            );
          },
        }}
        onFinish={handleSubmit}
      >
        <ProForm.Group>
          <ProFormText
            name="userAccount"
            label="账户"
            placeholder="请输入账户名"
            rules={[{required: true, message: '账户名是必填项'}]}
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormSelect
            name="userRole"
            label="用户角色"
            options={[
              {label: '普通用户', value: 0},
              {label: '管理员', value: 1},
            ]}
            rules={[{required: true, message: '用户角色是必选项'}]}
          />
        </ProForm.Group>
      </ProForm>
    </Modal>

  )
}

export default CreateUserModal;
