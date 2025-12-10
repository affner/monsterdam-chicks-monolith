import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { UserLanguage } from 'app/shared/model/enumerations/user-language.model';
import { createEntity, getEntity, reset, updateEntity } from './user-settings.reducer';

export const UserSettingsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userSettingsEntity = useAppSelector(state => state.userSettings.entity);
  const loading = useAppSelector(state => state.userSettings.loading);
  const updating = useAppSelector(state => state.userSettings.updating);
  const updateSuccess = useAppSelector(state => state.userSettings.updateSuccess);
  const userLanguageValues = Object.keys(UserLanguage);

  const handleClose = () => {
    navigate(`/user-settings${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.messageBlurIntensity !== undefined && typeof values.messageBlurIntensity !== 'number') {
      values.messageBlurIntensity = Number(values.messageBlurIntensity);
    }
    if (values.sessionsActiveCount !== undefined && typeof values.sessionsActiveCount !== 'number') {
      values.sessionsActiveCount = Number(values.sessionsActiveCount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...userSettingsEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          language: 'ES',
          ...userSettingsEntity,
          createdDate: convertDateTimeFromServer(userSettingsEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userSettingsEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.userSettings.home.createOrEditLabel" data-cy="UserSettingsCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.userSettings.home.createOrEditLabel">Create or edit a UserSettings</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="user-settings-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.darkMode')}
                id="user-settings-darkMode"
                name="darkMode"
                data-cy="darkMode"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.language')}
                id="user-settings-language"
                name="language"
                data-cy="language"
                type="select"
              >
                {userLanguageValues.map(userLanguage => (
                  <option value={userLanguage} key={userLanguage}>
                    {translate(`monsterdamChicksApp.UserLanguage.${userLanguage}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.contentFilter')}
                id="user-settings-contentFilter"
                name="contentFilter"
                data-cy="contentFilter"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.messageBlurIntensity')}
                id="user-settings-messageBlurIntensity"
                name="messageBlurIntensity"
                data-cy="messageBlurIntensity"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.activityStatusVisibility')}
                id="user-settings-activityStatusVisibility"
                name="activityStatusVisibility"
                data-cy="activityStatusVisibility"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.twoFactorAuthentication')}
                id="user-settings-twoFactorAuthentication"
                name="twoFactorAuthentication"
                data-cy="twoFactorAuthentication"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.sessionsActiveCount')}
                id="user-settings-sessionsActiveCount"
                name="sessionsActiveCount"
                data-cy="sessionsActiveCount"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.emailNotifications')}
                id="user-settings-emailNotifications"
                name="emailNotifications"
                data-cy="emailNotifications"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.importantSubscriptionNotifications')}
                id="user-settings-importantSubscriptionNotifications"
                name="importantSubscriptionNotifications"
                data-cy="importantSubscriptionNotifications"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.newMessages')}
                id="user-settings-newMessages"
                name="newMessages"
                data-cy="newMessages"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.postReplies')}
                id="user-settings-postReplies"
                name="postReplies"
                data-cy="postReplies"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.postLikes')}
                id="user-settings-postLikes"
                name="postLikes"
                data-cy="postLikes"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.newFollowers')}
                id="user-settings-newFollowers"
                name="newFollowers"
                data-cy="newFollowers"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.smsNewStream')}
                id="user-settings-smsNewStream"
                name="smsNewStream"
                data-cy="smsNewStream"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.toastNewComment')}
                id="user-settings-toastNewComment"
                name="toastNewComment"
                data-cy="toastNewComment"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.toastNewLikes')}
                id="user-settings-toastNewLikes"
                name="toastNewLikes"
                data-cy="toastNewLikes"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.toastNewStream')}
                id="user-settings-toastNewStream"
                name="toastNewStream"
                data-cy="toastNewStream"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.siteNewComment')}
                id="user-settings-siteNewComment"
                name="siteNewComment"
                data-cy="siteNewComment"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.siteNewLikes')}
                id="user-settings-siteNewLikes"
                name="siteNewLikes"
                data-cy="siteNewLikes"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.siteDiscountsFromFollowedUsers')}
                id="user-settings-siteDiscountsFromFollowedUsers"
                name="siteDiscountsFromFollowedUsers"
                data-cy="siteDiscountsFromFollowedUsers"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.siteNewStream')}
                id="user-settings-siteNewStream"
                name="siteNewStream"
                data-cy="siteNewStream"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.siteUpcomingStreamReminders')}
                id="user-settings-siteUpcomingStreamReminders"
                name="siteUpcomingStreamReminders"
                data-cy="siteUpcomingStreamReminders"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.createdDate')}
                id="user-settings-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.lastModifiedDate')}
                id="user-settings-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.createdBy')}
                id="user-settings-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.lastModifiedBy')}
                id="user-settings-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userSettings.isDeleted')}
                id="user-settings-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-settings" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UserSettingsUpdate;
