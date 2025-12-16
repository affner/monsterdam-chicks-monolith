import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getAssistanceTickets } from 'app/entities/assistance-ticket/assistance-ticket.reducer';
import { ReportStatus } from 'app/shared/model/enumerations/report-status.model';
import { ReportCategory } from 'app/shared/model/enumerations/report-category.model';
import { createEntity, getEntity, reset, updateEntity } from './user-report.reducer';

export const UserReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const assistanceTickets = useAppSelector(state => state.assistanceTicket.entities);
  const userReportEntity = useAppSelector(state => state.userReport.entity);
  const loading = useAppSelector(state => state.userReport.loading);
  const updating = useAppSelector(state => state.userReport.updating);
  const updateSuccess = useAppSelector(state => state.userReport.updateSuccess);
  const reportStatusValues = Object.keys(ReportStatus);
  const reportCategoryValues = Object.keys(ReportCategory);

  const handleClose = () => {
    navigate(`/user-report${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAssistanceTickets({}));
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);
    if (values.reporterId !== undefined && typeof values.reporterId !== 'number') {
      values.reporterId = Number(values.reporterId);
    }
    if (values.reportedId !== undefined && typeof values.reportedId !== 'number') {
      values.reportedId = Number(values.reportedId);
    }
    if (values.multimediaId !== undefined && typeof values.multimediaId !== 'number') {
      values.multimediaId = Number(values.multimediaId);
    }
    if (values.messageId !== undefined && typeof values.messageId !== 'number') {
      values.messageId = Number(values.messageId);
    }
    if (values.postId !== undefined && typeof values.postId !== 'number') {
      values.postId = Number(values.postId);
    }
    if (values.commentId !== undefined && typeof values.commentId !== 'number') {
      values.commentId = Number(values.commentId);
    }

    const entity = {
      ...userReportEntity,
      ...values,
      ticket: assistanceTickets.find(it => it.id.toString() === values.ticket?.toString()),
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
          deletedDate: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING',
          reportCategory: 'POST_REPORT',
          ...userReportEntity,
          createdDate: convertDateTimeFromServer(userReportEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userReportEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(userReportEntity.deletedDate),
          ticket: userReportEntity?.ticket?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.userReport.home.createOrEditLabel" data-cy="UserReportCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.userReport.home.createOrEditLabel">Create or edit a UserReport</Translate>
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
                  id="user-report-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.reportDescription')}
                id="user-report-reportDescription"
                name="reportDescription"
                data-cy="reportDescription"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.status')}
                id="user-report-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {reportStatusValues.map(reportStatus => (
                  <option value={reportStatus} key={reportStatus}>
                    {translate(`monsterdamChicksApp.ReportStatus.${reportStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.createdDate')}
                id="user-report-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.lastModifiedDate')}
                id="user-report-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.createdBy')}
                id="user-report-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.lastModifiedBy')}
                id="user-report-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.deletedDate')}
                id="user-report-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.reportCategory')}
                id="user-report-reportCategory"
                name="reportCategory"
                data-cy="reportCategory"
                type="select"
              >
                {reportCategoryValues.map(reportCategory => (
                  <option value={reportCategory} key={reportCategory}>
                    {translate(`monsterdamChicksApp.ReportCategory.${reportCategory}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.reporterId')}
                id="user-report-reporterId"
                name="reporterId"
                data-cy="reporterId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.reportedId')}
                id="user-report-reportedId"
                name="reportedId"
                data-cy="reportedId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.multimediaId')}
                id="user-report-multimediaId"
                name="multimediaId"
                data-cy="multimediaId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.messageId')}
                id="user-report-messageId"
                name="messageId"
                data-cy="messageId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.postId')}
                id="user-report-postId"
                name="postId"
                data-cy="postId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userReport.commentId')}
                id="user-report-commentId"
                name="commentId"
                data-cy="commentId"
                type="text"
              />
              <ValidatedField
                id="user-report-ticket"
                name="ticket"
                data-cy="ticket"
                label={translate('monsterdamChicksApp.userReport.ticket')}
                type="select"
              >
                <option value="" key="0" />
                {assistanceTickets
                  ? assistanceTickets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-report" replace color="info">
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

export default UserReportUpdate;
