import React from 'react';
import {Avatar, message} from "antd";
import {ProForm, ProFormDatePicker} from "@ant-design/pro-form";
import {FooterToolbar, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {useModel} from "@@/exports";
import {flushSync} from "react-dom";
import {updateUser} from "@/services/ant-design-pro/api";

const UserProfileForm = () => {
  const {initialState, setInitialState} = useModel('@@initialState');

  const fetchUserInfo = async () => {
    const userInfo = await initialState?.fetchUserInfo?.();
    if (userInfo) {
      flushSync(() => {
        setInitialState((s) => ({
          ...s,
          currentUser: userInfo,
        }));
      });
    }
  };

  const handleSubmit = async (values: API.CurrentUser) => {
    const success = await updateUser(values); // 模拟 API 更新用户信息
    if (success) {
      message.success('用户信息更新成功');
      await fetchUserInfo();
    } else {
      message.error('更新失败，请重试');
    }
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'center', marginBottom: 24 }}>
        <Avatar size={100} src={initialState!.currentUser!.avatarUrl} />
      </div>
      <ProForm
        layout="vertical"
        initialValues={initialState!.currentUser}
        submitter={{
          render: (_, dom) => <FooterToolbar>{dom}</FooterToolbar>,
        }}
        onFinish={handleSubmit}
      >
        <ProForm.Group>
          <ProFormText
            name="id"
            label="用户 ID"
            disabled
          />
          <ProFormText
            name="username"
            label="用户名"
            placeholder="请输入用户名"
            rules={[{ required: true, message: '用户名是必填项' }]}
          />
          <ProFormText
            name="userAccount"
            label="用户账号"
            disabled
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormText
            name="email"
            label="邮箱"
            placeholder="请输入邮箱"
            rules={[{ type: 'email', message: '请输入有效的邮箱地址' }]}
          />
          <ProFormText
            name="phone"
            label="手机号"
            placeholder="请输入手机号"
          />
          <ProFormSelect
            name="gender"
            label="性别"
            options={[
              { label: '男', value: 1 },
              { label: '女', value: 2 },
            ]}
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormDatePicker
            name="createTime"
            label="注册时间"
            disabled
          />
          <ProFormSelect
            name="userStatus"
            label="用户状态"
            options={[
              { label: '正常', value: 0 },
              { label: '禁用', value: 1 },
            ]}
            disabled
          />
          <ProFormSelect
            name="userRole"
            label="用户角色"
            options={[
              { label: '普通用户', value: 0 },
              { label: '管理员', value: 1 },
            ]}
            disabled={true}
          />
        </ProForm.Group>
      </ProForm>
    </div>
  );
};

export default UserProfileForm;
